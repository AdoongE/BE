package com.adoonge.seedzip.bookmark.repository;

import com.adoonge.seedzip.bookmark.domain.CategoryBookmark;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.member.domain.Member;

@Repository
public interface CategoryBookmarkRepository extends JpaRepository<CategoryBookmark, Long> {

	Boolean existsByCategoryAndMember(Category category, Member member);

	@Query("SELECT cb FROM CategoryBookmark cb " +
		"JOIN FETCH cb.category c " +
		"WHERE cb.member.id = :memberId")
	List<CategoryBookmark> findAllByMemberId(@Param("memberId") Long memberId);
}
