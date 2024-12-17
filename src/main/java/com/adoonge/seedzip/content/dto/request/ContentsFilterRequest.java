package com.adoonge.seedzip.content.dto.request;

import com.adoonge.seedzip.content.domain.ContentsDataType;
import java.util.List;

public record ContentsFilterRequest(
        ContentsDataType dataType,
        List<String> tags,
        String keyword
) {
}
