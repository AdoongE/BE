package com.adoonge.seedzip.app.dto.response;

import java.time.LocalDate;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.dto.SeedStatisticsDTO;

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
	public static AppMainResponse from(
		Member member,
		Long todaySeedCount,
		SeedStatisticsDTO seedStatisticsDTO
	){
		return AppMainResponse.builder()
			.localDate(LocalDate.now())
			.userName(member.getNickname())
			.todaySeedCount(todaySeedCount)
			.totalSeedCount(seedStatisticsDTO.totalSeedCount())
			.mostReadSeedCount(seedStatisticsDTO.mostReadSeedCount())
			.neverReadSeedCount(seedStatisticsDTO.neverReadSeedCount())
			.build();
	}
}
