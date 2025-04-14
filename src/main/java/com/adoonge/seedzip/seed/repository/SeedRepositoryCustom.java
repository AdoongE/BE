package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.dto.reqeust.SeedFilteringRequest;
import java.awt.print.Pageable;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SeedRepositoryCustom {
    List<Seed> findSeedsByFiltering(Member member, Pageable pageable, SeedFilteringRequest request);
}
