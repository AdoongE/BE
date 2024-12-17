package com.adoonge.seedzip.filter.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.filter.dto.AddFilterRequest;
import com.adoonge.seedzip.filter.service.FilterService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/filter")
@RequiredArgsConstructor
@Tag(name = "FilterController", description = "맞춤 필터 관련 API")
public class FilterController {

	private final FilterService filterService;

	@PostMapping
	@Operation(summary = "필터 생성 API", description = "필터 생성 API입니다.")
	public ApiResponse<Void> createFilter(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody AddFilterRequest request) {

		filterService.createFilter(request, customUserDetails.getMember());

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/{filterId}")
	@Operation(summary = "필터를 통한 컨텐츠 조회 API", description = "필터를 통해 컨텐츠를 조회하는 API입니다.")
	public ApiResponse<Void> getContentsByFilter(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long filterId)
	{
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}




}
