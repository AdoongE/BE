package com.adoonge.seedzip.tag.dto;

import com.adoonge.seedzip.tag.domain.MemberTag;

public record TagResponse(
	Long id,
	String name
) {
	public static TagResponse from(MemberTag memberTag) {
		return new TagResponse(
			memberTag.getId(),
			memberTag.getTagName()
		);
	}


}
