package com.adoonge.seedzip.bookmark.repository;

import java.awt.print.Book;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adoonge.seedzip.bookmark.domain.Bookmark;
import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.member.domain.Member;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Boolean existsByCategoryAndMember(Category category, Member member);
}
