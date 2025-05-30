package com.adoonge.seedzip.app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.app.dto.response.AppMainResponse;
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

	@GetMapping("/main")
	@Operation(summary = "앱 메인페이지 유저 활동 정보 API", description = "앱 메인페이지에서 유저의 활동 정보를 가져오는 API")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = AppMainResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<AppMainResponse> getAllSeeds(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Member member = customUserDetails.getMember();
		return new ApiResponse<>(appService.getAppMain(member));
	}
}
