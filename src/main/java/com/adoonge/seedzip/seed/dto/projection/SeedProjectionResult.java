package com.adoonge.seedzip.seed.dto.projection;

import java.util.List;
import java.util.Map;


public record SeedProjectionResult(
	Map<Long, String> thumbnailMap,
	Map<Long, List<Long>> categoryIdMap,
	Map<Long, List<String>> categoryNameMap,
	Map<Long, List<Long>> tagIdMap,
	Map<Long, List<String>> tagNameMap
) {
}
