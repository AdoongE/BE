package com.adoonge.seedzip.bookmark.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.bookmark.domain.Bookmark;
import com.adoonge.seedzip.bookmark.dto.response.BookmarkResponse;
import com.adoonge.seedzip.bookmark.repository.BookmarkRepository;
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

	private final BookmarkRepository bookmarkRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public BookmarkResponse addBookmark(Long categoryId, Member member) {
		// 카테고리 존재 여부 확인
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> SeedzipException.from(ErrorCode.CATEGORY_NOT_FOUND));

		// 북마크 중복 확인
		if (bookmarkRepository.existsByCategoryAndMember(category, member)) {
			throw SeedzipException.from(ErrorCode.CATEGORY_ALREADY_BOOKMARKED);
		}

		Bookmark bookmark = Bookmark.of(category, member);
		Bookmark savedBookMark = bookmarkRepository.save(bookmark);	//id유무

		return BookmarkResponse.fromEntity(savedBookMark);
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

	public List<BookmarkResponse> getBookmarks(Member member) {
		List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberId(member.getId());

		return bookmarks.stream()
			.map(BookmarkResponse::fromEntity)
			.toList();
	}
}
