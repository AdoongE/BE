package com.adoonge.seedzip.seed.dto.request;

import java.time.LocalDate;

import com.adoonge.seedzip.seed.domain.SeedType;

import jakarta.validation.constraints.NotNull;

public record SeedRequest(
	@NotNull SeedType seedType,
	String seedName,    // 없으면 null
	@NotNull String[] boardCategories,
	Long thumbnailImage,	// 없으면 null
	String contentLink, // link만 해당, 나머지 null
	@NotNull String[] tags,
	LocalDate dDay,	// 없으면 null
	String seedDetail	// 없으면 null
) {
}
