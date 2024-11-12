package com.adoonge.seedzip.bookmark.dto.response;

import com.adoonge.seedzip.bookmark.domain.Bookmark;
import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;

public record BookmarkResponse(
	Long bookmarkId,
	Long categoryId,
	String name,
	Boolean isPublic,
	Long memberId
) {
	public static BookmarkResponse fromEntity(Bookmark bookmark) {
		Category category = bookmark.getCategory();
		return new BookmarkResponse(
			bookmark.getBookmarkId(),
			category.getCategoryId(),
			category.getName(),
			category.getVisibility() == Visibility.PUBLIC,
			bookmark.getMember().getId()
		);
	}
}
