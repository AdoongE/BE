// package com.adoonge.seedzip.content.repository;
//
// import com.adoonge.seedzip.content.domain.Document;
// import com.adoonge.seedzip.content.domain.Link;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;
//
// import java.util.List;
// import java.util.Optional;
//
// @Repository
// public interface LinkRepository extends JpaRepository<Link, Long> {
//     Optional<Link> findByContents_ContentsId(Long contentsId);
//
//     @Modifying
//     @Query("DELETE FROM Link l WHERE l.contents.contentsId = :contentsId")
//     void deleteByContentsId(@Param("contentsId") Long contentsId);
// }
