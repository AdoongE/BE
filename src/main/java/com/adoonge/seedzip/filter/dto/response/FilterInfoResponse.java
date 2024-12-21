package com.adoonge.seedzip.filter.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.filter.domain.FilterTag;
import com.adoonge.seedzip.tag.domain.Tag;

import lombok.Builder;

@Builder
public record FilterInfoResponse (

	List<String> storageFormats,
	List<String> tags,
	LocalDate startDate,
	LocalDate endDate,
	Long fromDDay,
	Long toDDay

){
	public static FilterInfoResponse from(Filter filter) {
		return FilterInfoResponse.builder()
			.storageFormats(filter.getStorageFormats())
			.tags(filter.getFilterTags().stream()
				.map(FilterTag::getTag)
				.map(Tag::getTagName)
				.toList())
			.startDate(filter.getStartDate())
			.endDate(filter.getEndDate())
			.fromDDay(filter.getFromDDay())
			.toDDay(filter.getToDDay())
			.build();

	}
}
