// package com.adoonge.seedzip.content.repository;
//
// import com.adoonge.seedzip.content.domain.Contents;
// import com.adoonge.seedzip.content.domain.mapping.CategoryContent;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;
//
// import java.util.List;
//
// @Repository
// public interface CategoryContentRepository extends JpaRepository<CategoryContent, Long>  {
//     @Query("SELECT cc.category.categoryId FROM CategoryContent cc WHERE cc.contents.contentsId = :contentId")
//     List<Long> findCategoryIdsByContentId(@Param("contentId") Long contentId);
//
//     @Query("SELECT cc.contents.contentsId FROM CategoryContent cc WHERE cc.category.categoryId = :categoryId")
//     List<Long> findContentIdsByCategoryId(@Param("categoryId") Long categoryId);
//
//     List<CategoryContent> findAllByContents_ContentsId(Long contentsId);
//
//     void deleteByContents(Contents contents);
// }
