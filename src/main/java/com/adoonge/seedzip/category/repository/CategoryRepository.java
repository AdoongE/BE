package com.adoonge.seedzip.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByMemberId(Long memberId);
	Category findByMemberIdAndName(Long memberId, String name);
}
