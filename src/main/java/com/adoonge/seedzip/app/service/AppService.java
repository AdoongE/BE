package com.adoonge.seedzip.app.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.app.dto.response.AppMainResponse;
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

	public AppMainResponse getAppMain(Member member) {
		LocalDate today = LocalDate.now();

		return AppMainResponse.from(
			member,
			seedRepository.countByCreatedAtBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay()),
			seedRepositoryCustom.getSeedStatistics(member)
		);
	}
}
