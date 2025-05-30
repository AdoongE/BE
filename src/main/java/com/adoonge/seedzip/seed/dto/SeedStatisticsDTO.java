package com.adoonge.seedzip.seed.dto;

public record SeedStatisticsDTO(
	Long totalSeedCount,
	Long mostReadSeedCount,
	Long neverReadSeedCount
) {
}
