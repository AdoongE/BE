package com.adoonge.seedzip.content.repository;

import com.adoonge.seedzip.content.domain.Contents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Long> {
}
