package com.adoonge.seedzip.seed.dto.request;

import java.util.List;

public record SeedFilteringRequest(
        List<String> tagNames,
        String keyword
) {
}
