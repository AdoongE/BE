package com.adoonge.seedzip.content.repository;

import java.util.List;
import java.util.Optional;

import com.adoonge.seedzip.content.domain.Document;
import com.adoonge.seedzip.content.domain.Image;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	@Query("SELECT i FROM Document i WHERE i.contents.contentsId = :contentsId AND i.docThumbnail = :docThumbnail")
	Optional<Document> findByContentsIdAndDocThumbnail(Long contentsId, boolean docThumbnail);

	List<Document> findAllByContents_ContentsId(Long contentsId);

	@Modifying
	@Query("DELETE FROM Document d WHERE d.contents.contentsId = :contentsId")
	void deleteByContentsId(@Param("contentsId") Long contentsId);
}
