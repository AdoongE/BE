package com.adoonge.seedzip.category.dto.request;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.member.domain.Member;

public record AddCategoryRequest(
	String name,
	Boolean isPublic
) {
	public Category toEntity(Member member, Boolean isDefault) {
		return Category.builder()
			.name(name == null ? "새 카테고리" : name)
			.isPublic(isPublic)
			.member(member)
				.isDefault(isDefault)
			.build();
	}
}
