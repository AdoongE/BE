package com.adoonge.seedzip.faq.dto.request;

import com.adoonge.seedzip.faq.domain.Faq;
import com.adoonge.seedzip.faq.domain.FaqType;

public record FaqCreateRequest(
        FaqType type,
        String question,
        String answer,
        Integer orderIndex
) {
    public Faq toEntity() {
        return Faq.builder()
                .type(this.type)
                .question(this.question)
                .answer(this.answer)
                .orderIndex(this.orderIndex)
                .build();
    }
}
