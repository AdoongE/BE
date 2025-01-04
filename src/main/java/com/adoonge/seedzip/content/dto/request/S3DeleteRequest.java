package com.adoonge.seedzip.content.dto.request;

import java.util.List;

public record S3DeleteRequest(
        List<String> fileUrls
) {
}
