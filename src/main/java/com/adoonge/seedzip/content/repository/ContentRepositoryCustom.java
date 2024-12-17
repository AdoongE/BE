package com.adoonge.seedzip.content.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.QContents;
import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.filter.domain.QFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ContentRepositoryCustom {
	private final JPAQueryFactory queryFactory;



}
