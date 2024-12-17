package com.adoonge.seedzip.filter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adoonge.seedzip.filter.domain.Filter;

public interface FilterRepository extends JpaRepository<Filter, Long> {

}
