package com.adoonge.seedzip.filter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adoonge.seedzip.filter.domain.Filter;

public interface FilterRepository extends JpaRepository<Filter, Long> {

	List<Filter> findAllByMemberId(Long memberId);

	Optional<Filter> findByFilterIdAndMemberId(Long filterId, Long MemberId);

}
