package com.adoonge.seedzip.filter.repository;

import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.filter.domain.QFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
public interface FilterRepositoryCustom {
	Long findNextNumber();
}
