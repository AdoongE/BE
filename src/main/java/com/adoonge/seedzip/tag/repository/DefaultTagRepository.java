package com.adoonge.seedzip.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.tag.domain.CustomTag;
import com.adoonge.seedzip.tag.domain.DefaultTag;

@Repository
public interface DefaultTagRepository extends JpaRepository<DefaultTag, Long> {

	Optional<DefaultTag> findByTagName(String name);
}
