package com.adoonge.seedzip.bookmark.service;

import com.adoonge.seedzip.bookmark.domain.SeedBookmark;
import com.adoonge.seedzip.bookmark.repository.SeedBookmarkRepository;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.repository.SeedRepository;
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
	private final SeedBookmarkRepository seedBookmarkRepository;
	private final SeedRepository seedRepository;

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

	@Transactional
	public void addSeedBookmark(Long seedId, Member member) {
		Seed seed = seedRepository.findById(seedId)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.SEED_NOT_FOUND));

		if(seedBookmarkRepository.existsBySeedAndMember(seed, member)) {
			throw SeedzipException.from(ErrorCode.SEED_ALREADY_BOOKMARKED);
		}

		SeedBookmark seedBookmark = SeedBookmark.builder()
				.seed(seed)
				.member(member)
				.build();

		seedBookmarkRepository.save(seedBookmark);
	}

	@Transactional
	public void deleteSeedBookmark(Long seedId, Member member) {
		Seed seed = seedRepository.findById(seedId)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.SEED_NOT_FOUND));

		SeedBookmark seedBookmark = seedBookmarkRepository.findBySeedAndMember(seed, member)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.SEED_BOOKMARK_NOT_FOUND));

		seedBookmarkRepository.delete(seedBookmark);
	}

}
