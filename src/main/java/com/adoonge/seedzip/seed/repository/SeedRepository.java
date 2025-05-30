package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

public interface SeedRepository extends JpaRepository<Seed, Long> {
	Page<Seed> findByMember(Member member, Pageable pageable);

	Page<Seed> findByMemberAndSeedType(Member member, SeedType seedType, Pageable pageable);

	@Query("SELECT s FROM Seed s WHERE s.id IN :seedIds")
	Page<Seed> findBySeedIdIn(List<Long> seedIds, Pageable pageable);

	@Query("SELECT s FROM Seed s WHERE s.id IN :seedIds AND s.seedType = :seedType")
	Page<Seed> findBySeedIdInAndSeedType(List<Long> seedIds, SeedType seedType, Pageable pageable);

	@Transactional
	@Modifying
	@Query("DELETE FROM Seed s WHERE NOT EXISTS (SELECT 1 FROM CategorySeed cs WHERE cs.seed.id = s.id)")
	default void deleteUnreferencedContents() {}

	@Modifying
	@Query("UPDATE Seed s SET s.viewCount = s.viewCount + :count WHERE s.id = :seedId")
	void incrementViews(@Param("seedId") Long seedId, @Param("count") Long count);

	long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
