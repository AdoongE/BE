package com.adoonge.seedzip.term.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.term.dto.response.TermResponse;
import com.adoonge.seedzip.term.entity.Term;
import com.adoonge.seedzip.term.entity.TermType;
import com.adoonge.seedzip.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    public TermResponse getTermByType(TermType type) {
        Term term = termRepository.findByType(type)
                .orElseThrow(() -> SeedzipException.from(ErrorCode.TERM_NOT_FOUND));
        return TermResponse.from(term);
    }
}
