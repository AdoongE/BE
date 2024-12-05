package com.adoonge.seedzip.tag.dto;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.tag.domain.Tag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;

public record TagResponse(
	Long id,
	String name
) {

	public static TagResponse from(Tag tag) {
		return new TagResponse(
			tag.getId(),
			tag.getTagName()
		);
	}

	public static TagResponse from(UsedDefaultTag usedDefaultTag) {
		return new TagResponse(
			usedDefaultTag.getId(),
			usedDefaultTag.getTag().getTagName()
		);
	}


}
