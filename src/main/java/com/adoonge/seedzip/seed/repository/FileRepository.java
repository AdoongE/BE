package com.adoonge.seedzip.seed.repository;

import com.adoonge.seedzip.seed.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
