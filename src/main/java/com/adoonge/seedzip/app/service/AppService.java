package com.adoonge.seedzip.app.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.app.dto.response.AppUserStatisticsResponse;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.repository.SeedRepository;
import com.adoonge.seedzip.seed.repository.SeedRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppService {
	private final SeedRepositoryCustom seedRepositoryCustom;
	private final SeedRepository seedRepository;

	public AppUserStatisticsResponse getMemberStatistics(Member member) {
		LocalDate today = LocalDate.now();

		return AppUserStatisticsResponse.from(
			member,
			seedRepository.countByCreatedAtBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay()),
			seedRepositoryCustom.getSeedStatistics(member)
		);
	}
}
