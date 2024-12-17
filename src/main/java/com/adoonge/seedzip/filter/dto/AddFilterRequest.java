package com.adoonge.seedzip.filter.dto;

import java.util.List;
import java.time.LocalDate;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.filter.domain.Filter;


public record AddFilterRequest(
	String name,
	List<String> storageFormats,
	List<String> tags,
	LocalDate startDate,
	LocalDate endDate,
	Long dDayStart,
	Long dDayEnd
) {

	public Filter toEntity(Member member, Long number) {
		return Filter.builder()
			.name(number == 0L ? name : "새 필터 "+number)
			.storageFormats(storageFormats)
			.startDate(startDate)
			.endDate(endDate)
			.dDayStart(dDayStart)
			.dDayEnd(dDayEnd)
			.member(member)
			.filterNum(number)
			.build();
	}
}
