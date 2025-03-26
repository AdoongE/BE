package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.Seed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeedRepository extends JpaRepository<Seed, Long> {
}
