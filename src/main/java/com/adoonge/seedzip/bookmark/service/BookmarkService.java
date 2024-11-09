package com.adoonge.seedzip.bookmark.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.adoonge.seedzip.bookmark.domain.Bookmark;
import com.adoonge.seedzip.bookmark.dto.response.BookmarkResponse;
import com.adoonge.seedzip.bookmark.repository.BookmarkRepository;
import com.adoonge.seedzip.category.domain.Category;

import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public BookmarkResponse addBookmark(Long categoryId, Member member) {
		// 카테고리 존재 여부 확인
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));

		// 북마크 중복 확인
		Boolean isAlreadyBookmarked = bookmarkRepository.existsByCategoryAndMember(category, member);
		if (isAlreadyBookmarked) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ALREADY_BOOKMARKED);
		}

		Bookmark bookmark = Bookmark.builder()
			.category(category)
			.member(member)
			.build();
		bookmarkRepository.save(bookmark);

		return BookmarkResponse.fromEntity(bookmark);
	}

	@Transactional
	public void deleteBookmark(Long categoryId, Member member) {
		Bookmark bookmark = bookmarkRepository.findById(categoryId)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_BOOKMARK_NOT_FOUND));

		// 북마크 소유자 검증
		if (!bookmark.getMember().getId().equals(member.getId())) {
			throw SeedzipException.from(ErrorCode.CATEGORY_BOOKMARK_NOT_ALLOWED);
		}

		bookmarkRepository.delete(bookmark);
	}

	@Transactional
	public List<BookmarkResponse> getBookmarks(Member member) {
		List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(member.getId());

		return bookmarks.stream()
			.map(BookmarkResponse::fromEntity)
			.collect(Collectors.toList());
	}
}
