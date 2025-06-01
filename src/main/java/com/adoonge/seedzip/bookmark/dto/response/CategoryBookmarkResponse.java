package com.adoonge.seedzip.bookmark.dto.response;

import com.adoonge.seedzip.bookmark.domain.CategoryBookmark;

import lombok.Builder;

@Builder
public record CategoryBookmarkResponse(
	Long bookmarkId,
	Long categoryId,
	String name,
	Boolean isPublic,
	Long memberId
) {
	public static CategoryBookmarkResponse fromEntity(CategoryBookmark categoryBookmark) {
		return CategoryBookmarkResponse.builder()
			.bookmarkId(categoryBookmark.getId())
			.categoryId(categoryBookmark.getCategory().getCategoryId())
			.name(categoryBookmark.getCategory().getName())
			.isPublic(categoryBookmark.getCategory().getIsPublic())
			.memberId(categoryBookmark.getMember().getId())
			.build();
	}
}
