package com.adoonge.seedzip.notice.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.notice.domain.Notice;
import com.adoonge.seedzip.notice.dto.request.NoticeCreateRequest;
import com.adoonge.seedzip.notice.dto.request.NoticeUpdateRequest;
import com.adoonge.seedzip.notice.dto.response.NoticeDetailResponse;
import com.adoonge.seedzip.notice.dto.response.NoticeResponse;
import com.adoonge.seedzip.notice.repository.NoticeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public void createNotice(NoticeCreateRequest request, Member member) {

        if(checkAdmin(member)) {
            throw SeedzipException.from(ErrorCode.MEMBER_NOT_ADMIN);
        }

        Notice notice = NoticeCreateRequest.toEntity(request);

        noticeRepository.save(notice);
    }

    private boolean checkAdmin(Member member) {
        return member.getRole().equals("ADMIN");
    }

    @Transactional(readOnly = true)
    public Page<NoticeResponse> getNotices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.desc("isImportant"),
                Sort.Order.desc("createdAt")
        ));
        return noticeRepository.findByIsVisibleTrue(pageable)
                .map(NoticeResponse::from);
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponse getNoticeDetail(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId).orElseThrow(
                () -> SeedzipException.from(ErrorCode.NOTICE_NOT_FOUND));

        return NoticeDetailResponse.from(findNotice);
    }

    @Transactional
    public void updateNotice(Long noticeId ,NoticeUpdateRequest request, Member member) {

        if(checkAdmin(member)) {
            throw SeedzipException.from(ErrorCode.MEMBER_NOT_ADMIN);
        }

        Notice findNotice = noticeRepository.findById(noticeId).orElseThrow(
                () -> SeedzipException.from(ErrorCode.NOTICE_NOT_FOUND));

        findNotice.update(request);
    }
}

