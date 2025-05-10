package com.adoonge.seedzip.faq.service;

import com.adoonge.seedzip.faq.domain.Faq;
import com.adoonge.seedzip.faq.dto.request.FaqCreateRequest;
import com.adoonge.seedzip.faq.dto.request.FaqUpdateRequest;
import com.adoonge.seedzip.faq.dto.response.FaqResponse;
import com.adoonge.seedzip.faq.repository.FaqRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional(readOnly = true)
    public Page<FaqResponse> getFaqs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderIndex").ascending());
        return faqRepository.findAllByOrderByOrderIndexAsc(pageable)
                .map(FaqResponse::from);
    }

    @Transactional
    public void createFaq(FaqCreateRequest request, Member member) {

        if(!checkAdmin(member)){
            throw SeedzipException.from(ErrorCode.MEMBER_NOT_ADMIN);
        }

        if(isOrderIndexDuplicate(request.orderIndex())) {
            throw SeedzipException.from(ErrorCode.FAQ_INDEX_DUPLICATED);
        }

        Faq faq = request.toEntity();

        faqRepository.save(faq);
    }

    @Transactional
    public void updateFaq(Long faqId, FaqUpdateRequest request, Member member) {

        if(!checkAdmin(member)){
            throw SeedzipException.from(ErrorCode.MEMBER_NOT_ADMIN);
        }

        if(isOrderIndexDuplicate(request.orderIndex())) {
            throw SeedzipException.from(ErrorCode.FAQ_INDEX_DUPLICATED);
        }

        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> SeedzipException.from(ErrorCode.MEMBER_NOT_FOUND));

        faq.update(
                request.question(),
                request.answer(),
                request.type(),
                request.orderIndex()
        );
    }

    private boolean isOrderIndexDuplicate(Integer orderIndex) {
        return faqRepository.existsByOrderIndex(orderIndex);
    }

    private boolean checkAdmin(Member member) {
        return member.getRole().equals(Role.ADMIN);
    }
}
