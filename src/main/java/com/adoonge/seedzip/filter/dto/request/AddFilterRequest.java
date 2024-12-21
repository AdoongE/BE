package com.adoonge.seedzip.filter.dto.request;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

import com.adoonge.seedzip.content.domain.ContentsDataType;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.filter.domain.Filter;

import jakarta.annotation.Nullable;

public record AddFilterRequest(
	String name,
	@Nullable
	List<ContentsDataType> storageFormats,
	@Nullable
	List<String> tags,
	LocalDate startDate,
	LocalDate endDate,
	Long fromDDay,
	Long toDDay
) {

	public Filter toEntity(Member member, Long number) {
		if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw SeedzipException.from(ErrorCode.FILTER_CREATION_FAILED);
		}
		if(fromDDay != null && toDDay != null && fromDDay > toDDay) {
			throw SeedzipException.from(ErrorCode.FILTER_CREATION_FAILED);
		}

		return Filter.builder()
			.name(number == 0L ? name : "새 필터 "+number)
			.storageFormats(
				Optional.ofNullable(storageFormats)
					.orElse(Collections.emptyList()) // null이면 빈 리스트 반환
					.stream()
					.map(Enum::name)
					.toList()
			)
			.startDate(startDate)
			.endDate(endDate)
			.fromDDay(fromDDay)
			.toDDay(toDDay)
			.member(member)
			.filterNum(number)
			.build();
	}
}
