package com.adoonge.seedzip.filter.dto.response;

import com.adoonge.seedzip.filter.domain.Filter;

import lombok.Builder;

@Builder
public record FilterResponse(
	Long id,
	String name
) {

	public static FilterResponse from(Filter filter) {
		return FilterResponse.builder()
			.id(filter.getFilterId())
			.name(filter.getName())
			.build();
	}
}
