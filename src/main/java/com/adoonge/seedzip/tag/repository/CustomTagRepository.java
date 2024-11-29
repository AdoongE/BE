package com.adoonge.seedzip.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.tag.domain.CustomTag;

@Repository
public interface CustomTagRepository extends JpaRepository<CustomTag, Long> {

	Optional<CustomTag> findByTagNameAndMemberId(String name, Long id);

	Optional<List<CustomTag>> findByMemberId(Long id);

}
