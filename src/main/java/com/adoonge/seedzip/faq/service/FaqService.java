package com.adoonge.seedzip.faq.service;

import com.adoonge.seedzip.faq.dto.response.FaqResponse;
import com.adoonge.seedzip.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    public Page<FaqResponse> getFaqs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderIndex").ascending());
        return faqRepository.findAllByOrderByOrderIndexAsc(pageable)
                .map(FaqResponse::from);
    }

}
