package com.adoonge.seedzip.filter.repository;

import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.filter.domain.QFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FilterRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public Long findNextNumber(){
		QFilter filter = QFilter.filter;

		Long maxNumber = queryFactory.select(filter.filterNum.max())
			.from(filter)
			.fetchOne();

		return maxNumber + 1;
	}
}
