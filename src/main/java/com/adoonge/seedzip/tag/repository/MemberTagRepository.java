package com.adoonge.seedzip.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.tag.domain.MemberTag;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {

	Optional<List<MemberTag>> findByMemberId(Long id);
}
