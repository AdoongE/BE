package com.adoonge.seedzip.tag.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String name);

    List<Tag> findAllByMemberId(Long memberId);

    Optional<Tag> findByTagNameAndMemberId(String name, Long memberId);

    void deleteAllByMember(Member member);
}
