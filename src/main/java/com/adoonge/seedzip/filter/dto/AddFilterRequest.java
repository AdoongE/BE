package com.adoonge.seedzip.filter.dto;

import java.util.List;
import java.time.LocalDate;

import com.adoonge.seedzip.content.domain.ContentsDataType;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.filter.domain.Filter;


public record AddFilterRequest(
	String name,
	List<ContentsDataType> storageFormats,
	List<String> tags,
	LocalDate startDate,
	LocalDate endDate,
	Long fromDDay,
	Long toDDay
) {

	public Filter toEntity(Member member, Long number) {
		return Filter.builder()
			.name(number == 0L ? name : "새 필터 "+number)
			.storageFormats(storageFormats.stream().map(Enum::name).toList())
			.startDate(startDate)
			.endDate(endDate)
			.fromDDay(fromDDay)
			.toDDay(toDDay)
			.member(member)
			.filterNum(number)
			.build();
	}
}
