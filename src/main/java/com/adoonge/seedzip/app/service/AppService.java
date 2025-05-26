package com.adoonge.seedzip.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adoonge.seedzip.app.dto.response.AppMainResponse;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.service.MemberService;
import com.adoonge.seedzip.seed.service.SeedService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppService {

	private final MemberService memberService;
	private final SeedService seedService;

	public AppMainResponse getAppMain(Member member) {

		return null;

	}
}
