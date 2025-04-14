package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import com.adoonge.seedzip.seed.dto.reqeust.SeedFilteringRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SeedRepositoryCustom {
    Page<Seed> findSeedsByFiltering(Member member, Pageable pageable, SeedType seedType, SeedFilteringRequest request);
}
