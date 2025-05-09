package com.adoonge.seedzip.seed.dto.request;

import java.time.LocalDate;

import com.adoonge.seedzip.seed.domain.SeedType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SeedUpdateRequest(
	@NotNull SeedType seedType,
	String seedName,    // 없으면 null
	@NotNull String[] boardCategories,
	Long thumbnailImage,    // 없으면 null
	String seedLink, // link만 해당, 나머지 null
	@NotNull @Size(min = 2, message = "태그는 최소 2개 이상 입력해야 합니다.") String[] tags,
	LocalDate dDay,    // 없으면 null
	String seedDetail    // 없으면 null
) {
}
