package com.adoonge.seedzip.seed.dto.projection;

import com.adoonge.seedzip.seed.domain.SeedType;

public interface FileSeedProjection {
	Long getSeedId();
	SeedType getSeedType();
	String getLink();
	Boolean getIsThumbnail();
}
