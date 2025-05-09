package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.File;
import com.adoonge.seedzip.seed.domain.Seed;
import com.adoonge.seedzip.seed.dto.projection.FileSeedProjection;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.seed = :seed AND f.isThumbnail = true")
    Optional<File> findThumbnailBySeed(@Param("seed") Seed seed);

    Optional<File> findBySeed(Seed seed);

    Optional<List<File>> findAllBySeed(Seed seed);

    @Query("SELECT f.seed.id AS seedId, f.seed.seedType AS seedType, f.link AS link, f.isThumbnail AS isThumbnail " +
        "FROM File f WHERE f.seed.id IN :seedIds")
    List<FileSeedProjection> findFileInfoBySeedIds(@Param("seedIds") List<Long> seedIds);

    void deleteAllBySeedId(Long seedId);

    List<File> findFilesBySeedId(Long seedId);
}
