package com.adoonge.seedzip.member.repository;

import com.adoonge.seedzip.member.domain.Member;
import com.amazonaws.services.apigateway.model.Op;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);
}
