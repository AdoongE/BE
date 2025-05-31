package com.adoonge.seedzip.seed.repository;

import java.time.LocalDate;
import java.util.List;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import com.adoonge.seedzip.seed.dto.SeedStatisticsDTO;
import com.adoonge.seedzip.seed.dto.request.SeedFilteringRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SeedRepositoryCustom {
    Page<Seed> findSeedsByFiltering(Member member, Pageable pageable, SeedType seedType, SeedFilteringRequest request);
    Page<Seed> findCategorySeedsByFiltering(Member member, Pageable pageable, SeedType seedType,Long categoryId ,SeedFilteringRequest request);
    Page<Seed> findSeedsByCustomFilter(Pageable pageable, LocalDate startDate, LocalDate endDate,
        List<String> seedType, Long dDayStart, Long dDayEnd, Long filterId, Long memberId
    );

    SeedStatisticsDTO getSeedStatistics(Member member);
}
