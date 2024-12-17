package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.QContents;
import com.adoonge.seedzip.content.domain.mapping.QContentTag;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.QTag;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentsRepositoryImpl implements ContentsRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Contents> findContentsByFilters(Predicate predicate, Member member, List<String> tags) {
        QContents contents = QContents.contents;
        QContentTag contentTag = QContentTag.contentTag;
        QTag tag = QTag.tag;


        // 메인 쿼리
        JPAQuery<Contents> query = queryFactory
                .selectFrom(contents)
                .leftJoin(contentTag).on(contentTag.contents.eq(contents)) // ContentTag와 조인
                .leftJoin(contentTag.tag, tag) // Tag와 조인
                .where(contents.member.eq(member)
                        .and(predicate)) // 추가 필터 조건
                .distinct(); // 중복 제거

        // 태그 필터링 조건 추가 (필요할 경우에만)
        if (tags != null && !tags.isEmpty()) {
            query.groupBy(contents.contentsId)
                    .having(tag.countDistinct().goe(tags.size())); // 최소 태그 개수 조건
        }

        return query.fetch();
    }
}
