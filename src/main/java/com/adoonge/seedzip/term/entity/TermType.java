package com.adoonge.seedzip.term.entity;

import lombok.Getter;

@Getter
public enum TermType {
    SERVICE("서비스 이용약관"),
    PRIVACY("개인정보 처리방침"),
    MARKETING("광고성 정보 수신 동의");

    private final String description;

    TermType(String description) {
        this.description = description;
    }
}
