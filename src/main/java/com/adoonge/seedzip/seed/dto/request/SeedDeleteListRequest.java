package com.adoonge.seedzip.seed.dto.request;

import java.util.List;

public record SeedDeleteListRequest (
	List<Long> ids
){
}
