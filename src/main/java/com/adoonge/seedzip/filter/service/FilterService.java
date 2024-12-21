package com.adoonge.seedzip.filter.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.filter.domain.FilterTag;
import com.adoonge.seedzip.filter.dto.request.AddFilterRequest;
import com.adoonge.seedzip.filter.dto.request.UpdateFilterRequest;
import com.adoonge.seedzip.filter.dto.response.FilterInfoResponse;
import com.adoonge.seedzip.filter.dto.response.FilterResponse;
import com.adoonge.seedzip.filter.dto.request.UpdateFilterNameRequest;
import com.adoonge.seedzip.filter.repository.FilterRepository;
import com.adoonge.seedzip.filter.repository.FilterRepositoryCustom;
import com.adoonge.seedzip.filter.repository.FilterTagRepository;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.domain.Tag;
import com.adoonge.seedzip.tag.domain.type.DefaultTagType;
import com.adoonge.seedzip.tag.repository.TagRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
@Slf4j
public class FilterService {
	private final FilterRepository filterRepository;
	private final FilterRepositoryCustom filterRepositoryCustom;
	private final FilterTagRepository filterTagRepository;
	private final TagRepository tagRepository;

	@Transactional
	public void createFilter(AddFilterRequest request, Member member) {
		// 필터 생성 개수 제한
		// if (filterRepository.count() >= 5) {
		// 	throw SeedzipException.from(ErrorCode.FILTER_QUOTA_EXCEEDED);
		// }

		// name이 null이면 필터 이름을 생성하기 위해 넘버 할당
		Long nextNumber = request.name() == null ? filterRepositoryCustom.findNextNumber() : 0L;

		Filter savedFilter = filterRepository.save(request.toEntity(member, nextNumber));

		saveFilterTags(request.tags(), member, savedFilter);
	}


	public List<FilterResponse> getFilters(Member member) {
		List<Filter> filters = filterRepository.findAllByMemberId(member.getId());

		return filters.stream()
			.map(FilterResponse::from)
			.toList();
	}

	@Transactional
	public void deleteFilter(Member member, Long filterId) {
		Filter filter = filterRepository.findByFilterIdAndMemberId(filterId, member.getId())
			.orElseThrow(() -> SeedzipException.from(ErrorCode.FILTER_NOT_FOUND));

		filterRepository.delete(filter);
	}

	@Transactional
	public void updateFilterName(Member member, Long filterId, UpdateFilterNameRequest request) {
		Filter filter = filterRepository.findByFilterIdAndMemberId(filterId, member.getId())
			.orElseThrow(() -> SeedzipException.from(ErrorCode.FILTER_NOT_FOUND));

		filter.updateName(request.name());
	}

	public FilterInfoResponse getFilter(Long filterId, Member member) {
		Filter filter = filterRepository.findByFilterIdAndMemberId(filterId, member.getId())
			.orElseThrow(() -> SeedzipException.from(ErrorCode.FILTER_NOT_FOUND));

		return FilterInfoResponse.from(filter);
	}

	@Transactional
	public void updateFilter(Member member, Long filterId, UpdateFilterRequest request) {
		Filter filter = filterRepository.findByFilterIdAndMemberId(filterId, member.getId())
			.orElseThrow(() -> SeedzipException.from(ErrorCode.FILTER_NOT_FOUND));

		LocalDate startDate = request.startDate();
		LocalDate endDate = request.endDate();
		Long fromDDay = request.fromDDay();
		Long toDDay = request.toDDay();

		if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw SeedzipException.from(ErrorCode.FILTER_CREATION_FAILED);
		}
		if(fromDDay != null && toDDay != null && fromDDay > toDDay) {
			throw SeedzipException.from(ErrorCode.FILTER_CREATION_FAILED);
		}

		Filter updatedFilter = filter.update(
			Optional.ofNullable(request.storageFormats())
				.orElse(Collections.emptyList())
				.stream()
				.map(Enum::name)
				.toList(),
			startDate,
			endDate,
			fromDDay,
			toDDay
		);

		filterTagRepository.deleteByFilter(filter);	// 기존 태그 삭제
		saveFilterTags(request.tags(), member, updatedFilter);
	}

	private void saveFilterTags(List<String> tags, Member member, Filter savedFilter) {
		// 태그 저장
		if(tags != null){
			for (String tag : tags) {
				filterTagRepository.save(FilterTag.builder()
					.tag(findTag(tag, member))
					.filter(savedFilter)
					.build()
				);
			}
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
