package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.QSeed;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.SeedType;
import com.adoonge.seedzip.seed.domain.mapping.QSeedTag;
import com.adoonge.seedzip.seed.dto.reqeust.SeedFilteringRequest;
import com.adoonge.seedzip.tag.domain.QTag;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class SeedRepositoryImpl implements SeedRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "latest");

    QSeed seed = QSeed.seed;
    QSeedTag seedTag = QSeedTag.seedTag;
    QTag tag = QTag.tag;

    @Override
    public Page<Seed> findSeedsByFiltering(Member member, Pageable pageable, SeedType seedType, SeedFilteringRequest request) {
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

    //Tag 필터 조건
    private BooleanExpression filteringByTagNames(List<String> tagNames) {
        if(tagNames == null || tagNames.isEmpty()) {
            return null;
        }
        // STEP 1: 태그 조건을 만족하는 Seed ID 조회
        List<Long> seedIds = queryFactory
                .select(seedTag.seed.id)
                .from(seedTag)
                .join(seedTag.tag, tag)
                .where(tag.tagName.in(tagNames))
                .groupBy(seedTag.seed.id)
                .having(tag.tagName.countDistinct().eq((long) tagNames.size()))
                .fetch();

        return seed.id.in(seedIds);
    }

    //SeedType 필터조건
    private BooleanExpression filteringBySeedType(SeedType seedType) {
        return seedType != null ? seed.seedType.eq(seedType) : null;
    }

    //Keyword 필터 조건
    private BooleanExpression filteringByKeyword(String keyword) {
        return keyword != null && !keyword.isEmpty() ? seed.seedName.containsIgnoreCase(keyword).or(seed.seedDetail.containsIgnoreCase(keyword)) : null;
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
