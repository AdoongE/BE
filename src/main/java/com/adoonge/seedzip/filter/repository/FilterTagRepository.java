package com.adoonge.seedzip.filter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adoonge.seedzip.filter.domain.FilterTag;

public interface FilterTagRepository extends JpaRepository<FilterTag,Long> {
}
