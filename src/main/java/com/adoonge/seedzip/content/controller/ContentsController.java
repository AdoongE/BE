package com.adoonge.seedzip.content.controller;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.content.dto.request.ContentsFilterRequest;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.response.ContentsAllResponse;
import com.adoonge.seedzip.content.dto.response.ContentsAllResponse.contentsInfo;
import com.adoonge.seedzip.content.dto.response.ContentsDocResponse;
import com.adoonge.seedzip.content.dto.response.ContentsImageResponse;
import com.adoonge.seedzip.content.dto.response.ContentsLinkResponse;
import com.adoonge.seedzip.content.service.ContentsService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
@Tag(name = "ContentController", description = "콘텐츠 관련 API")
public class ContentsController {

	@Autowired
	private final ContentsService contentsService;

	@PostMapping(value = "/upload/{contentsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "콘텐츠 업로드 API", description = "콘텐츠 업로드 API입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ContentsDocResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<ContentsAllResponse.contentResponse> uploadContents(
			@PathVariable("contentsId") Long contentsId,
		@Parameter(description = "업로드할 파일 리스트", content = @Content(mediaType = "application/octet-stream"))
		@RequestParam(value = "file", required = false) List<MultipartFile> files,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		ContentsAllResponse.contentResponse createdContents = contentsService.uploadContents(contentsId, files, member);

		return new ApiResponse<>(createdContents);
	}

	@PostMapping(value = "/")
	@Operation(summary = "콘텐츠 생성 API", description = "콘텐츠 생성 API입니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ContentsDocResponse.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<ContentsAllResponse.contentResponse> createContents(
			@Parameter(description = "JSON 요청 데이터", content = @Content(mediaType = "application/json"))
			@RequestBody ContentsRequest.allContentsRequest request,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		ContentsAllResponse.contentResponse createdContents = contentsService.createContents(request, member);

		return new ApiResponse<>(createdContents);
	}

	@GetMapping("/")
	@Operation(summary = "전체 콘텐츠 모아보기 API", description = "전체 콘텐츠를 홈화면에서 조회하는 API입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ContentsAllResponse.getAllContents.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<ContentsAllResponse.getAllContents> getAllContents(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		List<ContentsAllResponse.contentsInfo> contentsInfo = contentsService.getAllContents(member);
		ContentsAllResponse.getAllContents getAllContents = new ContentsAllResponse.getAllContents().builder()
			.nickname(member.getNickname())
			.contentsInfoList(contentsInfo)
			.build();
		return new ApiResponse<>(getAllContents);
	}

	@GetMapping("/{categoryId}")
	@Operation(summary = "카테고리 내 콘텐츠 모아보기 API", description = "카테고리에 해당하는 콘텐츠를 조회하는 API입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ContentsAllResponse.getAllContents.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<ContentsAllResponse.getAllContents> getCategoryContents(
		@PathVariable("categoryId") Long categoryId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		List<ContentsAllResponse.contentsInfo> contentsInfo = contentsService.getCategoryContents(categoryId);
		ContentsAllResponse.getAllContents getAllContents = new ContentsAllResponse.getAllContents().builder()
			.nickname(member.getNickname())
			.contentsInfoList(contentsInfo)
			.build();
		return new ApiResponse<>(getAllContents);
	}

	@GetMapping("/all/{contentsId}")
	@Operation(summary = "콘텐츠 상세 보기 API", description = "콘텐츠 내용을 조회하는 API입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ContentsAllResponse.getContents.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<ContentsAllResponse.getContents> getContentsDetail(@PathVariable("contentsId") Long contentsId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		ContentsAllResponse.getContents getContents = contentsService.getContentsDetail(contentsId);
		return new ApiResponse<>(getContents);
	}

	@PatchMapping(value = "/{contentsId}")
	@Operation(summary = "콘텐츠 수정 API", description = "콘텐츠 내용을 수정하는 API입니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ContentsAllResponse.getContents.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<ContentsAllResponse.contentResponse> modifyContents(
			@PathVariable("contentsId") Long contentsId,
			@Parameter(description = "JSON 요청 데이터", content = @Content(mediaType = "application/json"))
			@RequestBody ContentsRequest.allContentsRequest request,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		ContentsAllResponse.contentResponse modifyContents = contentsService.modifyContents(request, contentsId, member);
		return new ApiResponse<>(modifyContents);
	}

	@PatchMapping(value = "/uploadModified/{contentsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "수정된 콘텐츠 업로드 API", description = "수정된 콘텐츠 업로드 API입니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ContentsDocResponse.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<ContentsAllResponse.contentResponse> uploadModifiedContents(
			@PathVariable("contentsId") Long contentsId,
			@Parameter(description = "업로드할 파일 리스트", content = @Content(mediaType = "application/octet-stream"))
			@RequestParam(value = "file", required = false) List<MultipartFile> files,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		ContentsAllResponse.contentResponse modifyContents = contentsService.uploadModifiedContents(contentsId, files, member);

		return new ApiResponse<>(modifyContents);
	}

	@DeleteMapping("/{contentsId}")
	@Operation(summary = "콘텐츠 삭제 API", description = "콘텐츠 삭제 API입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ContentsAllResponse.getContents.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ApiResponse<Void> deleteContent(@PathVariable("contentsId") Long contentsId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();

		contentsService.deleteContent(contentsId, member);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PostMapping("/filtering")
	@Operation(summary = "전체 콘텐츠 필터링 및 검색 API", description = "전체 콘텐츠 필터링 및 검색 API입니다.")
	public ApiResponse<ContentsAllResponse.contentsInfo> filterContent(@RequestBody ContentsFilterRequest request
			, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Member member = customUserDetails.getMember();

		return new ApiResponse<>(contentsService.getFilteredContents(member, request));
	}

	@PostMapping("/filtering/{categoryId}")
	@Operation(summary = "카테고리 내 콘텐츠 필터링 및 검색 API", description = "카테고리 내 콘텐츠 필터링 및 검색 API입니다.")
	public ApiResponse<ContentsAllResponse.contentsInfo> filterContent(@PathVariable("categoryId") Long categoryId,
			@RequestBody ContentsFilterRequest request
			, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Member member = customUserDetails.getMember();

		return new ApiResponse<>(contentsService.getFilteredCategoryContents(member, categoryId, request));
	}

}
