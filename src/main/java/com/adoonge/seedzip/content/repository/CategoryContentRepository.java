package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.mapping.CategoryContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryContentRepository extends JpaRepository<CategoryContent, Long>  {
}
