package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.mapping.CategorySeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorySeedRepository extends JpaRepository<CategorySeed, Long> {
}
