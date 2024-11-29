package com.adoonge.seedzip.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.tag.domain.CustomTag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;

@Repository
public interface UsedDefaultTagRepository extends JpaRepository<UsedDefaultTag, Long> {

	Optional<UsedDefaultTag> findByMemberIdAndTagId(Long id, Long tagId);

	Optional<List<UsedDefaultTag>> findByMemberId(Long id);
}
