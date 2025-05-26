package com.adoonge.seedzip.seed.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeedCacheService {

	@Autowired private RedisTemplate<String, Object> redisTemplate;

	private static final String SEED_VIEWS_KEY = "seed-views";

	public void increaseViewCounts(Long seedId) {
		redisTemplate.opsForHash().increment(SEED_VIEWS_KEY, seedId.toString(), 1);
	}

	public Map<Object, Object> getAllViewCounts() {
		return redisTemplate.opsForHash().entries(SEED_VIEWS_KEY);
	}

	public void clearAllViewCounts() {
		redisTemplate.delete(SEED_VIEWS_KEY);
	}
}
