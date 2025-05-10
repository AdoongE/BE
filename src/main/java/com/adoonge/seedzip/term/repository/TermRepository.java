package com.adoonge.seedzip.term.repository;

import com.adoonge.seedzip.term.entity.Term;
import com.adoonge.seedzip.term.entity.TermType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByType(TermType type);
}
