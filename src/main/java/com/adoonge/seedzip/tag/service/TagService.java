package com.adoonge.seedzip.tag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.CustomTag;
import com.adoonge.seedzip.tag.domain.DefaultTag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;
import com.adoonge.seedzip.tag.dto.TagResponse;
import com.adoonge.seedzip.tag.repository.CustomTagRepository;
import com.adoonge.seedzip.tag.repository.DefaultTagRepository;
import com.adoonge.seedzip.tag.repository.TagRepository;
import com.adoonge.seedzip.tag.repository.UsedDefaultTagRepository;
import com.amazonaws.services.apigatewayv2.model.Api;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
@Slf4j
@Transactional
public class TagService {

	private final DefaultTagRepository defaultTagRepository;
	private final CustomTagRepository customTagRepository;
	private final TagRepository tagRepository;
	private final UsedDefaultTagRepository usedDefaultTagRepository;

	// 사용자의 커스텀 태그 조회
	public ApiResponse<?> getCustomTag(Member member) {
		List<CustomTag> memberTags = customTagRepository.findByMemberId(member.getId()).orElse(List.of());

		// 태그가 없는 경우 메시지만 전송
		if (memberTags.isEmpty()) {
			return new ApiResponse<>(ErrorCode.EMPTY_TAG);
		}

		// 태그가 있는 경우 태그 리스트로 전송
		return new ApiResponse<>(memberTags.stream()
			.map(TagResponse::from)
			.toList());
	}

	// 사용자가 디폴트 태그 중 사용한 적 있는 것만 조회
	public ApiResponse<?> getUsedTags(Member member) {

		List<UsedDefaultTag> usedTags = usedDefaultTagRepository.findByMemberId(member.getId()).orElse(List.of());

		if (usedTags.isEmpty()) {
			return new ApiResponse<>(ErrorCode.EMPTY_USED_TAG);
		}

		return new ApiResponse<>(usedTags.stream()
			.map(TagResponse::from)
			.toList());
	}

	// 디폴트 태그 삽입
	public void saveDefaultTags(List<String> tagNames) {
		for (String tagName : tagNames) {
			defaultTagRepository.save(DefaultTag.builder()
				.name(tagName).build());
		}
	}

}
