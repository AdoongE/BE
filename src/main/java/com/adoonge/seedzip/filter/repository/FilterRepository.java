package com.adoonge.seedzip.filter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adoonge.seedzip.filter.domain.Filter;

public interface FilterRepository extends JpaRepository<Filter, Long> {

	List<Filter> findAllByMemberId(Long memberId);

}
