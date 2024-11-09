package com.adoonge.seedzip.bookmark.dto.response;

import com.adoonge.seedzip.bookmark.domain.Bookmark;
import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;

public record BookmarkResponse(
	Long bookmarkId,
	Long categoryId,
	String name,
	Visibility visibility,
	Long memberId
) {
	public static BookmarkResponse fromEntity(Bookmark bookmark) {
		Category category = bookmark.getCategory();
		return new BookmarkResponse(
			bookmark.getBookmarkId(),
			category.getCategoryId(),
			category.getName(),
			category.getVisibility(),
			bookmark.getMember().getId()
		);
	}
}
