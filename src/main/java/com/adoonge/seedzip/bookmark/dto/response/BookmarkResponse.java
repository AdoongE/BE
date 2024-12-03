package com.adoonge.seedzip.bookmark.dto.response;

import com.adoonge.seedzip.bookmark.domain.Bookmark;

import lombok.Builder;

@Builder
public record BookmarkResponse(
	Long bookmarkId,
	Long categoryId,
	String name,
	Boolean isPublic,
	Long memberId
) {
	public static BookmarkResponse fromEntity(Bookmark bookmark) {
		return BookmarkResponse.builder()
			.bookmarkId(bookmark.getBookmarkId())
			.categoryId(bookmark.getCategory().getCategoryId())
			.name(bookmark.getCategory().getName())
			.isPublic(bookmark.getCategory().getIsPublic())
			.memberId(bookmark.getMember().getId())
			.build();
	}
}
