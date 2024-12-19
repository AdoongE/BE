package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.Tag;
import com.querydsl.core.types.Predicate;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentsRepositoryCustom {
    List<Contents> findContentsByFilters(Predicate predicate, Member member, List<String> tags);
    List<Contents> findCategoryContentsByFilters(Predicate predicate, Member member, Long categoryId, List<String> tags);

   List<Contents> findContentsByCustomFilter(LocalDate startDate, LocalDate endDate,
       List<String> storageFormats, Long dDayStart, Long dDayEnd, Long filterId, Long memberId
   );
}
