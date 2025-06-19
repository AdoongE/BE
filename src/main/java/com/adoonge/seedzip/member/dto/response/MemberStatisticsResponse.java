package com.adoonge.seedzip.member.dto.response;

import java.time.LocalDate;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.dto.SeedStatisticsDTO;

import lombok.Builder;

@Builder
public record MemberStatisticsResponse(
        LocalDate localDate,
        String userName,
        Long todaySeedCount,
        Long totalSeedCount,
        Long mostReadSeedCount,
        Long neverReadSeedCount,
        Long totalCategoryCount
) {
    public static MemberStatisticsResponse from(
            Member member,
            Long todaySeedCount,
            SeedStatisticsDTO seedStatisticsDTO,
            Long totalCategoryCount
    ) {
        return MemberStatisticsResponse.builder()
                .localDate(LocalDate.now())
                .userName(member.getNickname())
                .todaySeedCount(todaySeedCount)
                .totalSeedCount(seedStatisticsDTO.totalSeedCount())
                .mostReadSeedCount(seedStatisticsDTO.mostReadSeedCount())
                .neverReadSeedCount(seedStatisticsDTO.neverReadSeedCount())
                .totalCategoryCount(totalCategoryCount)
                .build();
    }
}
