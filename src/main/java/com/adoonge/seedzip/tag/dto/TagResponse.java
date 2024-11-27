package com.adoonge.seedzip.tag.dto;

import com.adoonge.seedzip.tag.domain.MemberTag;
import com.adoonge.seedzip.tag.domain.UsedTag;

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

	public static TagResponse from(UsedTag usedTag) {
		return new TagResponse(
			usedTag.getId(),
			usedTag.getTag().getTagName()
		);
	}


}
