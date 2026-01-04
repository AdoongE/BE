package com.adoonge.seedzip.member.service;

import com.adoonge.seedzip.category.repository.CategoryRepository;
import java.time.LocalDate;

import com.adoonge.seedzip.filter.repository.FilterRepository;
import com.adoonge.seedzip.member.dto.response.MemberStatisticsResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.dto.request.UpdateMemberRequest;
import com.adoonge.seedzip.member.dto.response.MemberInformationResponse;
import com.adoonge.seedzip.member.repository.MemberRepository;
import com.adoonge.seedzip.seed.repository.SeedRepository;
import com.adoonge.seedzip.seed.repository.SeedRepositoryCustom;
import com.adoonge.seedzip.seed.repository.SeedTagRepository;
import com.adoonge.seedzip.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SeedRepository seedRepository;
    private final SeedRepositoryCustom seedRepositoryCustom;
    private final CategoryRepository categoryRepository;
    private final FilterRepository filterRepository;
    private final TagRepository tagRepository;
    private final SeedTagRepository seedTagRepository;

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
        seedRepository.findAllByMember(member).forEach(seed -> {
            seedTagRepository.deleteAllBySeedId(seed.getId());
        });
        tagRepository.deleteAllByMember(member);

        categoryRepository.deleteAllByMember(member);
        filterRepository.deleteAllByMember(member);
        seedRepository.deleteAllByMember(member);
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberStatisticsResponse getMemberStatistics(Member member) {
        LocalDate today = LocalDate.now();

        Long totalCategoryCount = categoryRepository.countByMember(member);

        return MemberStatisticsResponse.from(
                member,
                seedRepository.countByCreatedAtBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay()),
                seedRepositoryCustom.getSeedStatistics(member),
                totalCategoryCount
        );
    }
}
