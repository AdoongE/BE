package com.adoonge.seedzip.bookmark.repository;

import com.adoonge.seedzip.bookmark.domain.SeedBookmark;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeedBookmarkRepository extends JpaRepository<SeedBookmark, Long> {

    Boolean existsBySeedAndMember(Seed seed, Member member);
}
