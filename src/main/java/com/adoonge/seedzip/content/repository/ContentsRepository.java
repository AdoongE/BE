package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Contents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Contents findByContentsId(Long contentsId);
    List<Contents> findByMemberId(Long memberId);
    List<Contents> findByContentsIdIn(List<Long> contentIds);
}
