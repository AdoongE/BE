package com.adoonge.seedzip.filter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.filter.domain.Filter;
import com.adoonge.seedzip.filter.dto.AddFilterRequest;
import com.adoonge.seedzip.filter.repository.FilterRepository;
import com.adoonge.seedzip.filter.repository.FilterRepositoryCustom;
import com.adoonge.seedzip.member.domain.Member;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class FilterService {
	private final FilterRepository filterRepository;
	private final FilterRepositoryCustom filterRepositoryCustom;

	@Transactional
	public void createFilter(AddFilterRequest request, Member member) {

		// name이 null이면 필터 이름을 생성하기 위해 넘버 할당
		Long nextNumber = request.name() == null ? filterRepositoryCustom.findNextNumber() : 0L;

		Filter filter = request.toEntity(member, nextNumber);

		filterRepository.save(filter);
	}
}
