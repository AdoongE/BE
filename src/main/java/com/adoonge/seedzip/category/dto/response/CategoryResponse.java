package com.adoonge.seedzip.category.dto.response;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;
import com.adoonge.seedzip.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class CategoryResponse {

	private Long categoryId;
	private String name;
	private Visibility visibility;
	private Long memberId;

	public static CategoryResponse fromEntity(Category category) {
		return CategoryResponse.builder()
			.categoryId(category.getCategoryId())
			.name(category.getName())
			.visibility(category.getVisibility())
			.memberId(category.getMember().getId())
			.build();
	}
}
