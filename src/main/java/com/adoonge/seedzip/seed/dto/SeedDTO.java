package com.adoonge.seedzip.seed.dto;

import java.time.LocalDate;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SeedDTO(
	@NotNull SeedType type,
	@NotNull String name,
	Long thumbnailImage,    // 없으면 null
	LocalDate dDay,    // 없으면 null
	String detail,   // 없으면 null
	Member member
) {

	public Seed toEntity() {
		return Seed.builder()
			.seedName(name)
			.dDay(dDay)
			.seedDetail(detail)
			.thumbnailIdx(thumbnailImage)
			.seedType(type)
			.member(member)
			.build();
	}
}
