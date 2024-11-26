package com.adoonge.seedzip.tag.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.response.ContentsAllResponse;
import com.adoonge.seedzip.content.dto.response.ContentsDocResponse;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.dto.TagResponse;
import com.adoonge.seedzip.tag.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
@Tag(name = "TagController", description = "태그 관련 API")
public class TagController {

	private final TagService tagService;

	@PostMapping(value = "/member")
	@Operation(summary = "사용자가 생성한 태그 조회 API", description = "콘텐츠 생성 API입니다.")
	public ApiResponse<?> getMemberTag(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		ApiResponse<?> memberTags = tagService.getMemberTags(member);

		return new ApiResponse<>(memberTags);
	}


}
