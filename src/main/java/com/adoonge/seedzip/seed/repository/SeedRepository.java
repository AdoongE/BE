package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeedRepository extends JpaRepository<Seed, Long> {
    Page<Seed> findByMember(Member member, Pageable pageable);
    Page<Seed> findByMemberAndSeedType(Member member, SeedType seedType, Pageable pageable);

    @Query("SELECT s FROM Seed s WHERE s.id IN :seedIds")
    Page<Seed> findBySeedIdIn(List<Long> seedIds, Pageable pageable);
    @Query("SELECT s FROM Seed s WHERE s.id IN :seedIds AND s.seedType = :seedType")
    Page<Seed> findBySeedIdInAndSeedType(List<Long> seedIds, SeedType seedType, Pageable pageable);

}
