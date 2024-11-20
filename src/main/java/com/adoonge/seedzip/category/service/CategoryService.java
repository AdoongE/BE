package com.adoonge.seedzip.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.dto.request.AddCategoryRequest;
import com.adoonge.seedzip.category.dto.request.UpdateCategoryRequest;
import com.adoonge.seedzip.category.dto.response.CategoryResponse;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
@Slf4j
@Transactional
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryResponse createCategory(AddCategoryRequest request, Member member) {
		Category category = categoryRepository.save(request.toEntity(member));
		return CategoryResponse.fromEntity(category);
	}

	public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request, Member member) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));

		// 카테고리 소유자 검증
		if (!category.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ACCESS_DENIED);
		}

		// 요청에 이름이 있으면 카테고리 업데이트
		if (request.name() != null) {
			category.updateCategory(request.name());
		}

		return CategoryResponse.fromEntity(category);
	}

	public List<CategoryResponse> getCategories(Member member) {
		List<Category> categories = categoryRepository.findByMemberId(member.getId());

		return categories.stream()
			.map(CategoryResponse::fromEntity)
			.collect(Collectors.toList());
	}

	public void deleteCategory(Long id, Member member) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));

		// 카테고리 소유자 검증
		if (!category.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ACCESS_DENIED);
		}

		categoryRepository.delete(category);
	}
}
