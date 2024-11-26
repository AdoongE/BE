package com.adoonge.seedzip.tag.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.dto.response.CategoryResponse;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.MemberTag;
import com.adoonge.seedzip.tag.dto.TagResponse;
import com.adoonge.seedzip.tag.repository.MemberTagRepository;
import com.adoonge.seedzip.tag.repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
@Slf4j
@Transactional
public class TagService {

	private final MemberTagRepository memberTagRepository;

	public ApiResponse<?> getMemberTags(Member member) {
		List<MemberTag> memberTags = memberTagRepository.findByMemberId(member.getId()).orElse(List.of());

		// 태그가 없는 경우 메시지만 전송
		if(memberTags.isEmpty()){
			return new ApiResponse<>(ErrorCode.EMPTY_TAG);
		}

		// 태그가 있는 경우 태그 리스트로 전송
		return new ApiResponse<>(memberTags.stream()
			.map(TagResponse::from)
			.toList());
	}
}
