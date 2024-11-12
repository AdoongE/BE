package com.adoonge.seedzip.category.dto.response;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public record CategoryResponse(

	Long categoryId,
	String name,
	Boolean isPublic,
	Long memberId) {

	public static CategoryResponse fromEntity(Category category) {
		return new CategoryResponse(
			category.getCategoryId(),
			category.getName(),
			category.getVisibility() == Visibility.PUBLIC,
			category.getMember().getId()
		);
	}
}
