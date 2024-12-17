package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.member.domain.Member;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentsRepositoryCustom {
    List<Contents> findContentsByFilters(Predicate predicate, Member member, List<String> tags);
}
