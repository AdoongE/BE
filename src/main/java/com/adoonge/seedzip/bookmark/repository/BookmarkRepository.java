package com.adoonge.seedzip.bookmark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.bookmark.domain.Bookmark;
import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.member.domain.Member;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Boolean existsByCategoryAndMember(Category category, Member member);

	@Query("SELECT b FROM Bookmark b " +
		"JOIN FETCH b.category c " +
		"WHERE b.member.id = :memberId")
	List<Bookmark> findByMemberId(@Param("memberId") Long memberId);
}
