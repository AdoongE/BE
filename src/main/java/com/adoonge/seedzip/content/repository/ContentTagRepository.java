package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.mapping.ContentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTagRepository extends JpaRepository<ContentTag, Long> {
}
