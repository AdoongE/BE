package com.adoonge.seedzip.faq.dto.request;

import com.adoonge.seedzip.faq.domain.FaqType;

public record FaqUpdateRequest(
        String question,
        String answer,
        FaqType type,
        Integer orderIndex
) {}
