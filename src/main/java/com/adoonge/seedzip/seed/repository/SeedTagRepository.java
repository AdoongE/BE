package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.domain.mapping.SeedTag;
import com.adoonge.seedzip.seed.dto.projection.SeedTagProjection;

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

    @Query("SELECT st.seed.id as seedId, st.tag.id as tagId, st.tag.tagName as tagName " +
        "FROM SeedTag st WHERE st.seed.id IN :seedIds")
    List<SeedTagProjection> findTagInfoBySeedIds(@Param("seedIds") List<Long> seedIds);

    void deleteSeedTagsBySeedId(Long seedId);

    List<SeedTag> findSeedTagsBySeedId(Long seedId);
}
