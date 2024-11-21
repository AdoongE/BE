package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Contents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Contents findByContentsId(Long contentsId);

    @Query("SELECT c FROM Contents c WHERE c.member.id = :memberId ORDER BY c.contentsId DESC")
    List<Contents> findByMemberId(Long memberId);

    @Query("SELECT c FROM Contents c WHERE c.contentsId IN :contentIds ORDER BY c.contentsId DESC")
    List<Contents> findByContentsIdIn(List<Long> contentIds);
}
