package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.mapping.ContentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentTagRepository extends JpaRepository<ContentTag, Long> {
    @Query("SELECT ct.tag.tagId FROM ContentTag ct WHERE ct.contents.contentsId = :contentId")
    List<Long> findTagIdsByContentId(@Param("contentId") Long contentId);

    List<ContentTag> findAllByContents_ContentsId(Long contentId);

    void deleteByContents(Contents contents);
}
