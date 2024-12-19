package com.adoonge.seedzip.filter.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.filter.domain.FilterTag;
import com.adoonge.seedzip.filter.dto.AddFilterRequest;
import com.adoonge.seedzip.filter.repository.FilterRepository;
import com.adoonge.seedzip.filter.repository.FilterRepositoryCustom;
import com.adoonge.seedzip.filter.repository.FilterTagRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.Tag;
import com.adoonge.seedzip.tag.domain.UsedDefaultTag;
import com.adoonge.seedzip.tag.domain.type.DefaultTagType;
import com.adoonge.seedzip.tag.repository.TagRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class FilterService {
	private final FilterRepository filterRepository;
	private final FilterRepositoryCustom filterRepositoryCustom;
	private final FilterTagRepository filterTagRepository;
	private final TagRepository tagRepository;

	@Transactional
	public void createFilter(AddFilterRequest request, Member member) {
		// 필터 생성 제한
		// if (filterRepository.count() >= 5) {
		// 	throw SeedzipException.from(ErrorCode.FILTER_QUOTA_EXCEEDED);
		// }

		// name이 null이면 필터 이름을 생성하기 위해 넘버 할당
		Long nextNumber = request.name() == null ? filterRepositoryCustom.findNextNumber() : 0L;

		Filter save = filterRepository.save(request.toEntity(member, nextNumber));

		// 태그 저장
		for (String tag : request.tags()) {
			filterTagRepository.save(FilterTag.builder()
				.tag(findTag(tag, member))
				.filter(save)
				.build()
			);
		}

	}

	private Tag findTag(String tagName, Member member) {
		// 1. Default Tag인 경우
		if (Arrays.stream(DefaultTagType.values())
			.anyMatch(tag -> tag.getDisplayName().equals(tagName))) {

			return tagRepository.findByTagName(tagName)
				.orElseThrow(() -> SeedzipException.from(ErrorCode.TAG_NOT_FOUND));
		}

		// 2. 디폴트 태그가 아닌 경우 CustomTag에서 찾기
		return tagRepository.findByTagNameAndMemberId(tagName, member.getId())
			.orElseThrow(() -> SeedzipException.from(ErrorCode.TAG_NOT_FOUND));
	}
}
