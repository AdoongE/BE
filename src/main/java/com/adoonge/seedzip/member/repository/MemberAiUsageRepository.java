package com.adoonge.seedzip.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.member.domain.MemberAiUsages;

@Repository
public interface MemberAiUsageRepository extends JpaRepository<MemberAiUsages, Long> {

	Optional<MemberAiUsages> findByMemberId(Long memberId);
}
