package com.adoonge.seedzip.content.repository;

import java.util.List;

import com.adoonge.seedzip.content.domain.Document;
import com.adoonge.seedzip.content.domain.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	List<Document> findAllByContents_ContentsId(Long contentsId);
}
