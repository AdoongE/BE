package com.adoonge.seedzip.faq.domain;

import lombok.Getter;

@Getter
public enum FaqType {
    SERVICE("서비스"),
    PAYMENT("결제/환불");

    private final String description;

    FaqType(String description) {
        this.description = description;
    }

}
