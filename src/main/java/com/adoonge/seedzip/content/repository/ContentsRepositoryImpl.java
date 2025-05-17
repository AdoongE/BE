// package com.adoonge.seedzip.content.repository;
//
// import com.adoonge.seedzip.category.domain.QCategory;
// import com.adoonge.seedzip.content.domain.Contents;
// import com.adoonge.seedzip.content.domain.ContentsDataType;
// import com.adoonge.seedzip.content.domain.QContents;
// import com.adoonge.seedzip.content.domain.mapping.QCategoryContent;
// import com.adoonge.seedzip.content.domain.mapping.QContentTag;
// import com.adoonge.seedzip.filter.domain.Filter;
// import com.adoonge.seedzip.filter.domain.QFilterTag;
// import com.adoonge.seedzip.member.domain.Member;
// import com.adoonge.seedzip.tag.domain.QTag;
// import com.adoonge.seedzip.tag.domain.Tag;
// import com.querydsl.core.types.Predicate;
// import com.querydsl.core.types.dsl.BooleanExpression;
// import com.querydsl.core.types.dsl.DatePath;
// import com.querydsl.core.types.dsl.DateTimePath;
// import com.querydsl.core.types.dsl.Expressions;
// import com.querydsl.jpa.impl.JPAQuery;
// import com.querydsl.jpa.impl.JPAQueryFactory;
//
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
//
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// public class ContentsRepositoryImpl implements ContentsRepositoryCustom {
//
// 	private final JPAQueryFactory queryFactory;
//
// 	@Override
// 	public List<Contents> findContentsByFilter(Predicate predicate, Member member, List<String> tags) {
// 		QContents contents = QContents.contents;
// 		QContentTag contentTag = QContentTag.contentTag;
// 		QTag tag = QTag.tag;
//
// 		// 메인 쿼리
// 		JPAQuery<Contents> query = queryFactory
// 			.selectFrom(contents)
// 			.leftJoin(contentTag).on(contentTag.contents.eq(contents)) // ContentTag와 조인
// 			.leftJoin(contentTag.tag, tag) // Tag와 조인
// 			.where(contents.member.eq(member)
// 				.and(predicate)) // 추가 필터 조건
// 			.distinct(); // 중복 제거
//
// 		// 태그 필터링 조건 추가 (필요할 경우에만)
// 		if (tags != null && !tags.isEmpty()) {
// 			query.groupBy(contents.contentsId)
// 				.having(tag.countDistinct().goe(tags.size())); // 최소 태그 개수 조건
// 		}
//
// 		return query.fetch();
// 	}
//
// 	@Override
// 	public List<Contents> findCategoryContentsByFilter(Predicate predicate, Member member, Long categoryId,
// 													   List<String> tags) {
// 		QContents contents = QContents.contents;
// 		QContentTag contentTag = QContentTag.contentTag;
// 		QCategory category = QCategory.category;
// 		QCategoryContent categoryContent = QCategoryContent.categoryContent;
// 		QTag tag = QTag.tag;
//
// 		// 메인 쿼리
// 		JPAQuery<Contents> query = queryFactory
// 			.selectFrom(contents)
// 			.leftJoin(categoryContent).on(categoryContent.contents.eq(contents)) // CategoryContent와 조인
// 			.leftJoin(categoryContent.category, category) // Category와 조인
// 			.leftJoin(contentTag).on(contentTag.contents.eq(contents)) // ContentTag와 조인
// 			.leftJoin(contentTag.tag, tag) // Tag와 조인
// 			.where(contents.member.eq(member)
// 				.and(categoryContent.category.categoryId.eq(categoryId))
// 				.and(predicate)) // 추가 필터 조건
// 			.distinct(); // 중복 제거
//
// 		// 태그 필터링 조건 추가 (필요할 경우에만)
// 		if (tags != null && !tags.isEmpty()) {
// 			query.groupBy(contents.contentsId)
// 				.having(tag.countDistinct().goe(tags.size())); // 최소 태그 개수 조건
// 		}
// 		System.out.println(query.fetch());
//
// 		return query.fetch();
// 	}
//
// 	@Override
// 	public List<Contents> findContentsByCustomFilter(LocalDate startDate, LocalDate endDate,
// 		List<String> storageFormats, Long dDayStart, Long dDayEnd, Long filterId, Long memberID) {
// 		QContents contents = QContents.contents;
//
// 		BooleanExpression condition = contents.member.id.eq(memberID);
//
// 		condition = safeAnd(condition, filterByDate(startDate, endDate));
// 		condition = safeAnd(condition, filterByDDay(dDayStart, dDayEnd));
// 		condition = safeAnd(condition, filterByStorageFormat(storageFormats));
// 		condition = safeAnd(condition, filterByTags(filterId));
//
// 		System.out.println(condition);
//
// 		return queryFactory.selectFrom(contents)
// 			.where(condition)
// 			.fetch();
// 	}
//
// 	// 날짜 필터 조건
// 	private BooleanExpression filterByDate(LocalDate startDate, LocalDate endDate) {
// 		DateTimePath<LocalDateTime> createdAt = QContents.contents.createdAt;
//
// 		if (startDate != null && endDate != null) {
// 			if(startDate.equals(endDate)) {
// 				return createdAt.between(startDate.atStartOfDay(), startDate.atStartOfDay().plusDays(1));
// 			}
// 			return createdAt.between(startDate.atStartOfDay(), endDate.atStartOfDay());
// 		} else if (startDate != null) {
// 			return createdAt.goe(startDate.atStartOfDay());
// 		} else if (endDate != null) {
// 			return createdAt.loe(endDate.atStartOfDay());
// 		}
// 		return null; // 조건이 없으면 null 반환
// 	}
//
// 	// 저장 형식 필터 조건
// 	private BooleanExpression filterByStorageFormat(List<String> storageFormats) {
// 		QContents contents = QContents.contents;
//
// 		if (storageFormats != null && !storageFormats.isEmpty()) {
// 			BooleanExpression expression = null;
//
// 			for (String storageFormat : storageFormats) {
// 				BooleanExpression condition = contents.contentsDataType.eq(ContentsDataType.valueOf(storageFormat));
// 				expression = (expression == null) ? condition : expression.or(condition);
// 			}
//
// 			return expression;
// 		}
// 		return null; // 필터 조건이 없으면 null 반환
// 	}
//
// 	// D-Day 필터 조건
// 	private BooleanExpression filterByDDay(Long fromDDay, Long toDDay) {
// 		QContents contents = QContents.contents;
//
// 		if (fromDDay != null && toDDay != null) {
// 			if(fromDDay.equals(toDDay)) {	// D-Day가 같은 경우
// 				return contents.dDay.eq(LocalDate.now().plusDays(fromDDay));
// 			}
// 			return contents.dDay.between(LocalDate.now().plusDays(fromDDay), LocalDate.now().plusDays(toDDay));
// 		} else if (fromDDay != null) {
// 			return contents.dDay.goe(LocalDate.now().plusDays(fromDDay));	// D-Day 시작 범위
// 		} else if (toDDay != null) {
// 			return contents.dDay.loe(LocalDate.now().plusDays(toDDay));	// D-Day 끝 범위
// 		}
// 		return null;
// 	}
//
// 	// 태그 필터 조건
// 	private BooleanExpression filterByTags(Long filterId) {
// 		QContents contents = QContents.contents;
// 		QContentTag contentTag = QContentTag.contentTag;
// 		QFilterTag filterTag = QFilterTag.filterTag;
//
// 		if (filterId != null) {
// 			// 필터에 해당하는 태그 ID 조회
// 			List<Long> tagIds = queryFactory.select(filterTag.tag.id)
// 				.from(filterTag)
// 				.where(filterTag.filter.filterId.eq(filterId))
// 				.fetch();
//
// 			// 필터에 태그가 없으면 null 반환
// 			if(tagIds.isEmpty()) {
// 				return null;
// 			}
//
// 			// 콘텐츠에 필터 태그가 모두 포함되어야 함
// 			return contents.contentsId.in(
// 				queryFactory
// 					.select(contentTag.contents.contentsId)
// 					.from(contentTag)
// 					.where(contentTag.tag.id.in(tagIds))
// 					.groupBy(contentTag.contents.contentsId)
// 					.having(
// 						Expressions.asNumber(contentTag.tag.id.countDistinct()) // 콘텐츠에 있는 태그의 개수
// 							.eq((long) tagIds.size()) // 필터 태그 개수만큼 태그가 있어야 함
// 					)
// 			);
// 		} else
// 			return null;
// 	}
//
// 	private BooleanExpression safeAnd(BooleanExpression base, BooleanExpression condition) {
// 		return condition == null ? base : base.and(condition);
// 	}
//
// }