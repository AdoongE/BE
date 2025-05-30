package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.filter.domain.QFilterTag;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.QSeed;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import com.adoonge.seedzip.seed.domain.mapping.QCategorySeed;
import com.adoonge.seedzip.seed.domain.mapping.QSeedTag;
import com.adoonge.seedzip.seed.dto.SeedStatisticsDTO;
import com.adoonge.seedzip.seed.dto.request.SeedFilteringRequest;
import com.adoonge.seedzip.tag.domain.QTag;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class SeedRepositoryImpl implements SeedRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	QSeed seed = QSeed.seed;
	QSeedTag seedTag = QSeedTag.seedTag;
	QTag tag = QTag.tag;
	QCategorySeed categorySeed = QCategorySeed.categorySeed;
	QFilterTag filterTag = QFilterTag.filterTag;

	@Override
	public SeedStatisticsDTO getSeedStatistics(Member member) {
		Tuple result = queryFactory
			.select(
				seed.count(),
				new CaseBuilder()
					.when(seed.viewCount.goe(3L)).then(1)
					.otherwise(0)
					.sum(),
				new CaseBuilder()
					.when(seed.viewCount.eq(0L)).then(1)
					.otherwise(0)
					.sum()
			)
			.from(seed)
			.where(seed.member.eq(member))
			.fetchOne();

		if(result == null) {
			return new SeedStatisticsDTO(0L, 0L, 0L);
		}
		return new SeedStatisticsDTO(
			result.get(0, Long.class) != null ? result.get(0, Long.class) : 0L,
			result.get(1, Long.class) != null ? result.get(1, Long.class) : 0L,
			result.get(2, Long.class) != null ? result.get(2, Long.class) : 0L
		);
	}

	@Override
	public Page<Seed> findSeedsByFiltering(Member member, Pageable pageable, SeedType seedType,
		SeedFilteringRequest request) {
		List<String> tagNames = request.tags();
		String keyword = request.keyword();

		BooleanExpression condition = seed.member.eq(member);

		condition = safeAnd(condition, filteringByTagNames(tagNames));
		condition = safeAnd(condition, filteringByKeyword(keyword));
		condition = safeAnd(condition, filteringBySeedType(seedType));

		List<Seed> content = queryFactory
			.selectFrom(seed)
			.where(condition)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(getOrderSpecifiers(pageable.getSort()))
			.fetch();

		Long total = queryFactory
			.select(seed.count())
			.from(seed)
			.where(condition)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	@Override
	public Page<Seed> findCategorySeedsByFiltering(Member member, Pageable pageable, SeedType seedType, Long categoryId,
		SeedFilteringRequest request) {
		List<String> tagNames = request.tags();
		String keyword = request.keyword();

		BooleanExpression condition = seed.member.eq(member);

		condition = safeAnd(condition, filteringByCategory(categoryId));
		condition = safeAnd(condition, filteringByTagNames(tagNames));
		condition = safeAnd(condition, filteringByKeyword(keyword));
		condition = safeAnd(condition, filteringBySeedType(seedType));

		List<Seed> content = queryFactory
			.selectFrom(seed)
			.where(condition)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(getOrderSpecifiers(pageable.getSort()))
			.fetch();

		Long total = queryFactory
			.select(seed.count())
			.from(seed)
			.where(condition)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	@Override
	public Page<Seed> findSeedsByCustomFilter(Pageable pageable, LocalDate startDate, LocalDate endDate,
		List<String> seedType, Long dDayStart, Long dDayEnd, Long filterId, Long memberID) {

		BooleanExpression condition = seed.member.id.eq(memberID);
		condition = safeAnd(condition, filterByDate(startDate, endDate));
		condition = safeAnd(condition, filterByDDay(dDayStart, dDayEnd));
		condition = safeAnd(condition, filterByStorageFormat(seedType));
		condition = safeAnd(condition, filterByTags(filterId));

		// 1. 페이징 대상 Seed ID 조회
		// List<Long> seedIds = queryFactory
		//     .select(seed.id)
		//     .from(seed)
		//     .where(condition)
		//     .offset(pageable.getOffset())
		//     .limit(pageable.getPageSize())
		//     .fetch();
		//
		// // 2. 실제 Seed + 연관 데이터 조회 fetch join
		// List<Seed> seeds = queryFactory
		//     .selectDistinct(seed)
		//     .from(seed)
		//     .leftJoin(seed.files, file).fetchJoin()
		//     .leftJoin(seed.seedTags, seedTag).fetchJoin()
		//     .leftJoin(seedTag.tag, tag).fetchJoin()
		//     .leftJoin(seed.categorySeeds, categorySeed).fetchJoin()
		//     .leftJoin(categorySeed.category, category).fetchJoin()
		//     .where(seed.id.in(seedIds))
		//     .fetch();

		// 기존: fetch join으로 모든 걸 한 번에 가져옴 → 제거
		List<Seed> seeds = queryFactory
			.selectFrom(seed)
			.where(condition)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(seed.count())
			.from(seed)
			.where(condition)
			.fetchOne();

		return new PageImpl<>(seeds, pageable, total != null ? total : 0);
	}

	//Category 필터 조건
	private BooleanExpression filteringByCategory(Long categoryId) {
		if (categoryId == null) {
			return null;
		}
		// Category에 해당하는 Seed ID 조회
		List<Long> seedIds = queryFactory
			.select(categorySeed.seed.id)
			.from(categorySeed)
			.where(categorySeed.category.categoryId.eq(categoryId))
			.fetch();

		return seed.id.in(seedIds);
	}

	//Tag 필터 조건
	private BooleanExpression filteringByTagNames(List<String> tagNames) {
		if (tagNames == null || tagNames.isEmpty()) {
			return null;
		}
		// 태그 조건을 만족하는 Seed ID 조회
		List<Long> seedIds = queryFactory
			.select(seedTag.seed.id)
			.from(seedTag)
			.join(seedTag.tag, tag)
			.where(tag.tagName.in(tagNames))
			.groupBy(seedTag.seed.id)
			.having(tag.tagName.countDistinct().eq((long)tagNames.size()))
			.fetch();

		return seed.id.in(seedIds);
	}

	//SeedType 필터조건
	private BooleanExpression filteringBySeedType(SeedType seedType) {
		return seedType != null ? seed.seedType.eq(seedType) : null;
	}

	//Keyword 필터 조건
	private BooleanExpression filteringByKeyword(String keyword) {
		return keyword != null && !keyword.isEmpty() ?
			seed.seedName.containsIgnoreCase(keyword).or(seed.seedDetail.containsIgnoreCase(keyword)) : null;
	}

	// 날짜 필터 조건
	private BooleanExpression filterByDate(LocalDate startDate, LocalDate endDate) {
		DateTimePath<LocalDateTime> createdAt = seed.createdAt;

		if (startDate != null && endDate != null) {
			if (startDate.equals(endDate)) {
				return createdAt.between(startDate.atStartOfDay(), startDate.atStartOfDay().plusDays(1));
			}
			return createdAt.between(startDate.atStartOfDay(), endDate.atStartOfDay());
		} else if (startDate != null) {
			return createdAt.goe(startDate.atStartOfDay());
		} else if (endDate != null) {
			return createdAt.loe(endDate.atStartOfDay());
		}
		return null; // 조건이 없으면 null 반환
	}

	// 저장 형식 필터 조건
	private BooleanExpression filterByStorageFormat(List<String> seedType) {
		if (seedType == null || seedType.isEmpty()) {
			return null;
		}

		BooleanExpression expression = null;

		for (String typeStr : seedType) {
			try {
				SeedType type = SeedType.valueOf(typeStr);
				BooleanExpression condition = seed.seedType.eq(type);
				expression = (expression == null) ? condition : expression.or(condition);
			} catch (IllegalArgumentException e) {
				throw SeedzipException.from(ErrorCode.SEED_TYPE_NOT_SUPPORTED);
			}
		}

		return expression;
	}

	// D-Day 필터 조건
	private BooleanExpression filterByDDay(Long fromDDay, Long toDDay) {

		if (fromDDay != null && toDDay != null) {
			if (fromDDay.equals(toDDay)) {    // D-Day가 같은 경우
				return seed.dDay.eq(LocalDate.now().plusDays(fromDDay));
			}
			return seed.dDay.between(LocalDate.now().plusDays(fromDDay), LocalDate.now().plusDays(toDDay));
		} else if (fromDDay != null) {
			return seed.dDay.goe(LocalDate.now().plusDays(fromDDay));    // D-Day 시작 범위
		} else if (toDDay != null) {
			return seed.dDay.loe(LocalDate.now().plusDays(toDDay));    // D-Day 끝 범위
		}
		return null;
	}

	private BooleanExpression filterByTags(Long filterId) {
		if (filterId == null)
			return null;

		// 1. 필터에 포함된 태그 ID 리스트 조회
		List<Long> tagIds = queryFactory
			.select(filterTag.tag.id)
			.from(filterTag)
			.where(filterTag.filter.filterId.eq(filterId))
			.fetch();

		if (tagIds.isEmpty())
			return null;

		// 2. seedTag를 inner join하여 해당 태그들을 모두 포함하는 Seed만 조회
		return seed.id.in(
			queryFactory
				.selectDistinct(seedTag.seed.id)
				.from(seedTag)
				.where(seedTag.tag.id.in(tagIds))
				.groupBy(seedTag.seed.id)
				.having(seedTag.tag.id.countDistinct().goe((long)tagIds.size()))
		);
	}

	// 정렬 조건 변환
	private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();

		for (Sort.Order order : sort) {
			String sortBy = order.getProperty(); // ex) "latest" or "name"
			Order direction = order.isAscending() ? Order.ASC : Order.DESC;

			switch (sortBy) {
				case "createdAt" -> orders.add(new OrderSpecifier<>(direction, seed.createdAt));
				case "seedName" -> orders.add(new OrderSpecifier<>(direction, seed.seedName));
				default -> System.out.println("정렬 무시됨: " + sortBy);
			}
		}

		return orders.toArray(new OrderSpecifier[0]);
	}

	private BooleanExpression safeAnd(BooleanExpression base, BooleanExpression condition) {
		return condition == null ? base : base.and(condition);
	}
}
