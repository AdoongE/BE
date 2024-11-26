package com.adoonge.seedzip.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.tag.domain.UsedTag;

@Repository
public interface UsedTagRepository extends JpaRepository<UsedTag, Long> {
}
