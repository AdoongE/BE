package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeedRepository extends JpaRepository<Seed, Long> {
    Page<Seed> findByMember(Member member, Pageable pageable);
    Page<Seed> findByMemberAndSeedType(Member member, SeedType seedType, Pageable pageable);
}
