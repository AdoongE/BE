package com.adoonge.seedzip.seed.dto;

import java.time.LocalDate;

import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SeedDTO(
	@NotNull SeedType seedType,
	@NotNull String seedName,
	Long thumbnailImage,    // 없으면 null
	LocalDate dDay,    // 없으면 null
	String seedDetail    // 없으면 null
) {

	public Seed toEntity() {
		return Seed.builder()
			.seedName(seedName)
			.dDay(dDay)
			.seedDetail(seedDetail)
			.thumbnailIdx(thumbnailImage)
			.seedType(seedType)
			.build();
	}
}
