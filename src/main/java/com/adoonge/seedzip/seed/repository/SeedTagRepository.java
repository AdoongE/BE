package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.mapping.SeedTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeedTagRepository extends JpaRepository<SeedTag, Long> {

    @Query("SELECT st.tag.id FROM SeedTag st WHERE st.seed = :seed")
    List<Long> findTagIdsBySeed(@Param("seed") Seed seed);

    // 자동으로 Inner 조인 사용 , n+1 문제 발생 X
    @Query("SELECT st.tag.tagName FROM SeedTag st WHERE st.seed = :seed")
    List<String> findTagNamesBySeed(@Param("seed") Seed seed);

    List<SeedTag> findAllBySeedIdIn(List<Long> seedIds);
}
