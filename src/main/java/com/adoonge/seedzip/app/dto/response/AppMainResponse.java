package com.adoonge.seedzip.app.dto.response;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record AppMainResponse(
	LocalDate localDate,
	String userName,
	Long todaySeedCount,
	Long totalSeedCount,
	Long mostReadSeedCount,
	Long neverReadSeedCount
) {
}
