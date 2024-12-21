package com.adoonge.seedzip.filter.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.adoonge.seedzip.content.domain.ContentsDataType;

import jakarta.annotation.Nullable;

public record UpdateFilterRequest(
	@Nullable
	List<ContentsDataType> storageFormats,
	@Nullable
	List<String> tags,
	LocalDate startDate,
	LocalDate endDate,
	Long fromDDay,
	Long toDDay
) {
}
