package com.adoonge.seedzip.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.dto.request.AddCategoryRequest;
import com.adoonge.seedzip.category.dto.request.UpdateCategoryRequest;
import com.adoonge.seedzip.category.dto.response.CategoryResponse;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.repository.SeedRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final SeedRepository seedRepository;

	private static final String DEFAULT_CATEGORY_NAME = "미분류";


	@Transactional
	public CategoryResponse createCategory(AddCategoryRequest request, Member member) {

		// "미분류" 이름 사용 방지
		if (DEFAULT_CATEGORY_NAME.equals(request.name())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_NAME_RESERVED);
		}

		categoryRepository.findByMemberIdAndName(member.getId(), request.name()).
			ifPresent( category -> {
				throw SeedzipException.from(ErrorCode.CATEGORY_ALREADY_EXISTS);
			});

		Category category = categoryRepository.save(request.toEntity(member, false));
		return CategoryResponse.fromEntity(category);
	}

	@Transactional
	public CategoryResponse updateCategory(UpdateCategoryRequest request, Member member) {
		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));

		// 카테고리 소유자 검증
		if (!category.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ACCESS_DENIED);
		}

		// 미분류 카테고리 수정 불가
		if (Boolean.TRUE.equals(category.getIsDefault())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_CANNOT_BE_UPDATED);
		}

		category.updateCategoryName(request.name());

		return CategoryResponse.fromEntity(category);
	}

	public List<CategoryResponse> getCategories(Member member) {
		List<Category> categories = categoryRepository.findAllByMemberId(member.getId());

		return categories.stream()
			.map(CategoryResponse::fromEntity)
			.toList();
	}

	@Transactional
	public void deleteCategory(Long id, Member member) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));

		// 카테고리 소유자 검증
		if (!category.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ACCESS_DENIED);
		}

		// 미분류 카테고리 삭제 불가
		if (Boolean.TRUE.equals(category.getIsDefault())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_CANNOT_BE_DELETED);
		}

		// category, category_content 삭제
		categoryRepository.delete(category);
		categoryRepository.flush();	// 실행 순서 보장

		// 콘텐츠에서 참조되지 않는 항목을 삭제 (배치 처리)
		seedRepository.deleteUnreferencedContents();

	}
}
