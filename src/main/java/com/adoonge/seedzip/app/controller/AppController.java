package com.adoonge.seedzip.app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.app.dto.response.AppUserStatisticsResponse;
import com.adoonge.seedzip.app.service.AppService;
import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/app")
@RequiredArgsConstructor
@Tag(name = "AppController", description = "앱 전용 API")
public class AppController {

	private final AppService appService;

	@GetMapping("/member/statistics")
	@Operation(summary = "유저 씨드 정보 API", description = "유저의 씨드 정보를 가져오는 API입니다. 앱 메인페이지 및 카테고리 탭에서 사용됩니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = AppUserStatisticsResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<AppUserStatisticsResponse> getMemberStatistics(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Member member = customUserDetails.getMember();
		return new ApiResponse<>(appService.getMemberStatistics(member));
	}
}
