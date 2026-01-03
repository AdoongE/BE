package com.adoonge.seedzip.category.dto.response;

import com.adoonge.seedzip.category.domain.Category;

public record CategoryResponse(

	Long categoryId,
	String name,
	Boolean isPublic,
	Long memberId,
	Long seedCount) {

	public static CategoryResponse fromEntityWithCount(Category category, Long seedCount) {
		return new CategoryResponse(
			category.getCategoryId(),
			category.getName(),
			category.getIsPublic(),
			category.getMember().getId(),
			seedCount
		);
	}

	public static CategoryResponse fromEntity(Category category) {
		return new CategoryResponse(
			category.getCategoryId(),
			category.getName(),
			category.getIsPublic(),
			category.getMember().getId(),
			0L
		);
	}
}
