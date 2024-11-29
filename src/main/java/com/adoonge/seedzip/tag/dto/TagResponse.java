package com.adoonge.seedzip.tag.dto;

import com.adoonge.seedzip.tag.domain.CustomTag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;

public record TagResponse(
	Long id,
	String name
) {
	public static TagResponse from(CustomTag customTag) {
		return new TagResponse(
			customTag.getId(),
			customTag.getTagName()
		);
	}

	public static TagResponse from(UsedDefaultTag usedDefaultTag) {
		return new TagResponse(
			usedDefaultTag.getId(),
			usedDefaultTag.getTag().getTagName()
		);
	}


}
