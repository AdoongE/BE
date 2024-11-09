package com.adoonge.seedzip.bookmark.dto.response;

import com.adoonge.seedzip.bookmark.domain.Bookmark;

public record BookmarkResponse(
	Long bookmarkId,
	Long categoryId,
	Long memberId
) {
	public static BookmarkResponse fromEntity(Bookmark bookmark) {
		return new BookmarkResponse(
			bookmark.getBookmarkId(),
			bookmark.getCategory().getCategoryId(),
			bookmark.getMember().getId()
		);
	}
}
