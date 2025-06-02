package com.adoonge.seedzip.bookmark.repository;

import com.adoonge.seedzip.bookmark.domain.SeedBookmark;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeedBookmarkRepository extends JpaRepository<SeedBookmark, Long> {

    Boolean existsBySeedAndMember(Seed seed, Member member);

    Optional<SeedBookmark> findBySeedAndMember(Seed seed, Member member);

    @Query("SELECT sb.seed.id FROM SeedBookmark sb WHERE sb.member = :member")
    List<Long> findSeedIdsByMember(@Param("member") Member member);
}
