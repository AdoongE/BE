package com.adoonge.seedzip.term.dto.response;

import com.adoonge.seedzip.term.entity.Term;

public record TermResponse(String type,
                           String content) {
    public static TermResponse from(Term terms) {
        return new TermResponse(
                terms.getType().getDescription(),
                terms.getContent()
                );
    }
}
