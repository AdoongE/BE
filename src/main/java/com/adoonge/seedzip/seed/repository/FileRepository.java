package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.File;
import com.adoonge.seedzip.seed.domain.Seed;
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

    List<File> findAllBySeedIdIn(List<Long> seedIds);
}
