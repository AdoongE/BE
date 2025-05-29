package com.adoonge.seedzip.bookmark.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.bookmark.domain.CategoryBookmark;
import com.adoonge.seedzip.bookmark.dto.response.CategoryBookmarkResponse;
import com.adoonge.seedzip.bookmark.repository.CategoryBookmarkRepository;
import com.adoonge.seedzip.category.domain.Category;

import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkService {

	private final CategoryBookmarkRepository categoryBookmarkRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public CategoryBookmarkResponse addCategoryBookmark(Long categoryId, Member member) {
		// 카테고리 존재 여부 확인
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));

		// 북마크 중복 확인
		if (categoryBookmarkRepository.existsByCategoryAndMember(category, member)) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ALREADY_BOOKMARKED);
		}

		CategoryBookmark categoryBookmark = CategoryBookmark.of(category, member);
		CategoryBookmark savedBookMark = categoryBookmarkRepository.save(categoryBookmark);	//id유무

		return CategoryBookmarkResponse.fromEntity(savedBookMark);
	}

	@Transactional
	public void deleteCategoryBookmark(Long categoryId, Member member) {
		CategoryBookmark categoryBookmark = categoryBookmarkRepository.findById(categoryId)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_BOOKMARK_NOT_FOUND));

		// 북마크 소유자 검증
		if (!categoryBookmark.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_BOOKMARK_NOT_ALLOWED);
		}

		categoryBookmarkRepository.delete(categoryBookmark);
	}

	public List<CategoryBookmarkResponse> getCategoryBookmarks(Member member) {
		List<CategoryBookmark> categoryBookmarks = categoryBookmarkRepository.findAllByMemberId(member.getId());

		return categoryBookmarks.stream()
			.map(CategoryBookmarkResponse::fromEntity)
			.toList();
	}
}
