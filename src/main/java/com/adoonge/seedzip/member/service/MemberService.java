package com.adoonge.seedzip.member.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.dto.request.UpdateMemberRequest;
import com.adoonge.seedzip.member.dto.response.MemberInformationResponse;
import com.adoonge.seedzip.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberInformationResponse getMemberInformation(Member member) {
        return MemberInformationResponse.toDto(member);
    }

    @Transactional
    public void update(Member member, UpdateMemberRequest request) {
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> SeedzipException.from(ErrorCode.MEMBER_NOT_FOUND));

        findMember.update(request);
    }

    @Transactional
    public void delete(Member member) {
        memberRepository.delete(member);
    }
}
