package com.adoonge.seedzip.faq.repository;

import com.adoonge.seedzip.faq.domain.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    Page<Faq> findAllByOrderByOrderIndexAsc(Pageable pageable);
    boolean existsByOrderIndex(Integer orderIndex);
}
