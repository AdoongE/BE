package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Image;
import com.adoonge.seedzip.content.domain.Link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.contents.contentsId = :contentsId AND i.imgThumbnail = :imgThumbnail")
    Optional<Image> findByContentsIdAndImgThumbnail(Long contentsId, boolean imgThumbnail);

    List<Image> findAllByContents_ContentsId(Long contentsId);


}
