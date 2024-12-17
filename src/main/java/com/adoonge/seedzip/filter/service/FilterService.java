package com.adoonge.seedzip.filter.service;

import org.checkerframework.checker.units.qual.A;
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
		Long nextNumber = 0L;
		if(request.name() == null){
			nextNumber = filterRepositoryCustom.findNextNumber();
		}

		Filter filter = request.toEntity(member, nextNumber);

		filterRepository.save(filter);
	}
}
