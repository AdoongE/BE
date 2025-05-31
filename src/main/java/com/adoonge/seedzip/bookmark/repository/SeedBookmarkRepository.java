package com.adoonge.seedzip.bookmark.repository;

import com.adoonge.seedzip.bookmark.domain.SeedBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeedBookmarkRepository extends JpaRepository<SeedBookmark, Long> {
}
