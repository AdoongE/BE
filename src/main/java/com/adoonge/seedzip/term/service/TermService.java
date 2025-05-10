package com.adoonge.seedzip.term.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.domain.Role;
import com.adoonge.seedzip.term.dto.request.TermUpdateRequest;
import com.adoonge.seedzip.term.dto.response.TermResponse;
import com.adoonge.seedzip.term.entity.Term;
import com.adoonge.seedzip.term.entity.TermType;
import com.adoonge.seedzip.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    @Transactional(readOnly = true)
    public TermResponse getTermByType(TermType type) {
        Term term = termRepository.findByType(type)
                .orElseThrow(() -> SeedzipException.from(ErrorCode.TERM_NOT_FOUND));
        return TermResponse.from(term);
    }

    @Transactional
    public void updateTerm(TermType type, TermUpdateRequest request, Member member) {

        if(!member.getRole().equals(Role.USER)) {
            throw SeedzipException.from(ErrorCode.MEMBER_NOT_ADMIN);
        }

        Term term = termRepository.findByType(type)
                .orElseThrow(() -> SeedzipException.from(ErrorCode.TERM_NOT_FOUND));
        term.update(request.title(), request.content());
    }
}
