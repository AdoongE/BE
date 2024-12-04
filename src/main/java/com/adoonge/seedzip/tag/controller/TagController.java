package com.adoonge.seedzip.tag.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.category.dto.request.AddCategoryRequest;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.response.ContentsAllResponse;
import com.adoonge.seedzip.content.dto.response.ContentsDocResponse;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.tag.dto.TagResponse;
import com.adoonge.seedzip.tag.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

	@GetMapping(value = "/member")
	@Operation(summary = "사용자가 생성한 태그 조회 API", description = "태그 조회 API입니다.")
	public ApiResponse<TagResponse> getCustomTag(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		List<TagResponse> customTags = tagService.getCustomTag(member);

		return new ApiResponse<>(customTags);
	}

	@GetMapping(value = "/default/used")
	@Operation(summary = "기본 태그 중 사용한 적 있는 태그만 조회 API", description = "태그 조회 API입니다.")
	public ApiResponse<TagResponse> getUsedTag(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		List<TagResponse> usedTags = tagService.getUsedTags(member);

		return new ApiResponse<>(usedTags);
	}

	@PostMapping(value = "/default")
	@Operation(
		summary = "디폴트 태그 저장 API",
		description = "태그 저장 API입니다. admin 계정으로 로그인한 경우에만 사용 가능합니다.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "defaultTagsExample",
					summary = "Default Tags Example",
					value = """
                        [
                            "기획/아이디어",
                            "여행",
                            "글로벌",
                            "맛집",
                            "음식/요리",
                            "운동",
                            "건강",
                            "스포츠",
                            "영화/드라마",
                            "뮤지컬/연극",
                            "연예",
                            "음악",
                            "뷰티",
                            "패션",
                            "디자인",
                            "UX/UI",
                            "인테리어",
                            "사진",
                            "영상",
                            "SNS",
                            "IT",
                            "비즈니스",
                            "자기계발",
                            "생산성",
                            "생활",
                            "반려동물",
                            "책/글쓰기",
                            "취미",
                            "게임",
                            "공부",
                            "금융/재테크",
                            "부동산",
                            "예술",
                            "환경",
                            "역사",
                            "과학",
                            "철학",
                            "심리학",
                            "교육",
                            "정치"
                        ]
                        """
				)
			)
		)
	)
	public ApiResponse<Void> saveDefaultTag(@RequestBody List<String> tagNames, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Member member = customUserDetails.getMember();

		tagService.saveDefaultTags(member, tagNames);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}




}
