package com.adoonge.seedzip.faq.dto.response;

import com.adoonge.seedzip.faq.domain.Faq;

public record FaqResponse(
        Long id,
        String type,
        String question,
        String answer,
        Integer order
) {
    public static FaqResponse from(Faq faq) {
        return new FaqResponse(
                faq.getId(),
                faq.getType().getDescription(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getOrderIndex()
        );
    }
}
