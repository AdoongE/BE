package com.adoonge.seedzip.category.dto.request;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;
import com.adoonge.seedzip.member.domain.Member;

public record AddCategoryRequest(
	String name,
	Visibility visibility
) {
	public Category toEntity(Member member) {
		return Category.builder()
			.name(name)
			.visibility(visibility)
			.member(member)
			.build();
	}
}
