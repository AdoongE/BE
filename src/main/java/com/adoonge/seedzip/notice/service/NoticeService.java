package com.adoonge.seedzip.notice.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.notice.domain.Notice;
import com.adoonge.seedzip.notice.dto.request.NoticeCreateRequest;
import com.adoonge.seedzip.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
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
}
