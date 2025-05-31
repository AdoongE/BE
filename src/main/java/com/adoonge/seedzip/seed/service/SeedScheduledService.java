package com.adoonge.seedzip.seed.service;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.seed.repository.SeedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeedScheduledService {

	private final SeedCacheService seedCacheService;
	private final SeedRepository seedRepository;

	@Scheduled(cron = "* */10 * * * *") // 10분마다 실행
	@Transactional
	public void syncSeedViewCount() {
		// 캐시에서 시드 조회수 가져오기
		Map<Object, Object> allViewCounts = seedCacheService.getAllViewCounts();
		if (allViewCounts == null || allViewCounts.isEmpty()) {
			return; // 캐시에 조회수가 없으면 종료
		}

		// 시드 조회수 업데이트
		allViewCounts.forEach((k, v) -> {
			Long seedId = Long.parseLong(k.toString());
			Long viewCount = v == null ? 0L : Long.parseLong(v.toString());
			seedRepository.incrementViews(seedId, viewCount);
		});

		// 캐시 초기화
		seedCacheService.clearAllViewCounts();
	}

}
