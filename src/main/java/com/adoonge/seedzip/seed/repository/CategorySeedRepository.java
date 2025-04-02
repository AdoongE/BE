package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.mapping.CategorySeed;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategorySeedRepository extends JpaRepository<CategorySeed, Long> {

    @Query("SELECT cs.category.categoryId FROM CategorySeed cs WHERE cs.seed = :seed")
    List<Long> findCategoryIdsBySeed(@Param("seed") Seed seed);

    // 자동으로 Inner 조인 사용 , n+1 문제 발생 X
    @Query("SELECT cs.category.name FROM CategorySeed cs WHERE cs.seed = :seed")
    List<String> findCategoryNamesBySeed(@Param("seed") Seed seed);
}
