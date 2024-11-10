package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByContents_ContentsId(Long contentsId);
}
