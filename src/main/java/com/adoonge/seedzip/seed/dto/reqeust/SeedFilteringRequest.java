package com.adoonge.seedzip.seed.dto.reqeust;

import java.util.List;

public record SeedFilteringRequest(
        List<String> tags,
        String keyword
) {
}
