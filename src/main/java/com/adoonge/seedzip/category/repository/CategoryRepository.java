package com.adoonge.seedzip.category.repository;

import com.adoonge.seedzip.member.domain.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("""
        SELECT c
        FROM Category c
        WHERE c.member = :member
        ORDER BY c.isDefault DESC, c.createdAt DESC
    """)
	List<Category> findAllByMemberOrdered(@Param("member") Member member);

	Optional<Category> findByMemberIdAndName(Long memberId, String name);

	long countByMember(Member member);

	void deleteAllByMember(Member member);

}
