package com.adoonge.seedzip.tag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.Tag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;
import com.adoonge.seedzip.tag.dto.TagResponse;
import com.adoonge.seedzip.tag.repository.TagRepository;
import com.adoonge.seedzip.tag.repository.UsedDefaultTagRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
@Slf4j
@Transactional
public class TagService {

	private final TagRepository tagRepository;
	private final UsedDefaultTagRepository usedDefaultTagRepository;

	// 사용자의 커스텀 태그 조회
	public List<TagResponse> getCustomTag(Member member) {
		List<Tag> memberTags = tagRepository.findAllByMemberId(member.getId());

		return memberTags.stream()
			.map(TagResponse::from)
			.toList();
	}

	// 사용자가 디폴트 태그 중 사용한 적 있는 것만 조회
	public List<TagResponse> getUsedTags(Member member) {
		List<UsedDefaultTag> usedTags = usedDefaultTagRepository.findByMemberId(member.getId()).orElse(List.of());

		return usedTags.stream()
			.map(TagResponse::from)
			.toList();
	}

	// 디폴트 태그 삽입
	public void saveDefaultTags(Member member, List<String> tagNames) {
		if (!"admin".equals(member.getLoginId())) {
			throw SeedzipException.from(ErrorCode.MEMBER_NOT_ADMIN);
		}

		for (String tagName : tagNames) {
			tagRepository.save(Tag.builder().tagName(tagName).isDefault(true).member(member).build());
		}
	}
}


