package com.adoonge.seedzip.simplification.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.simplification.dto.response.SimplificationAllResponse;
import com.adoonge.seedzip.simplification.service.SimplificationService;
import com.adoonge.seedzip.simplification.service.YouTubeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/simplification")
@RequiredArgsConstructor
@Tag(name = "SimplificationController V2", description = "제목, 요약, 태그 간략화 관련 API V2")
public class SimplificationControllerV2 {

	private final SimplificationService simplificationService;

	private final YouTubeService youTubeService;

	/**
	 * s3 먼저 저장하는 버전
	 */
	@PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "이미지 간략화 관련 API(v2)", description = "이미지를 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
	public ApiResponse<SimplificationAllResponse.simplificationFileResponse> imageAnalysisV2(@Parameter(description = "업로드할 이미지", content = @Content(mediaType = "application/octet-stream"))
	@RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam(value = "thumbnailIdx", required = false) int thumbnailIdx) {
		SimplificationAllResponse.simplificationFileResponse response = simplificationService.requestImageAnalysisV2(files, thumbnailIdx);
		return new ApiResponse<>(response);
	}

	@PostMapping("/youtube")
	@Operation(summary = "유튜브 간략화 관련 API(v2)", description = "유튜브 링크를 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
	public ApiResponse<SimplificationAllResponse.simplificationLinkResponse> youtubeAnalysisV2(@RequestParam String youtubeUrl) throws
		IOException {
		String youtubeData = youTubeService.searchVideos(youtubeUrl);   // 유튜브 데이터 조회
		SimplificationAllResponse.simplificationLinkResponse response = simplificationService.requestTextAnalysisV2(youtubeData); // 요약 데이터 조회
		response.setLink(youtubeUrl);
		return new ApiResponse<>(response);
	}

	@PostMapping(value = "/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "PDF 간략화 관련 API(v2)", description = "PDF 파일을 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
	public ApiResponse<SimplificationAllResponse.simplificationFileResponse> pdfAnalysisV2(@Parameter(description = "업로드할 pdf", content = @Content(mediaType = "application/octet-stream"))
	@RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam(value = "thumbnailIdx", required = false) int thumbnailIdx) throws IOException {
		SimplificationAllResponse.simplificationFileResponse response = simplificationService.requestPdfAnalysisV2(files, thumbnailIdx);
		return new ApiResponse<>(response);
	}

	@PostMapping(value = "/naver-news")
	@Operation(summary = "네이버 뉴스 간략화 관련 API(v2)", description = "네이버 뉴스 링크를 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
	public ApiResponse<SimplificationAllResponse.simplificationLinkResponse> naverNewsAnalysisV2(@RequestParam String naverNewsUrl) throws IOException {
		SimplificationAllResponse.simplificationLinkResponse simplificationInfoResponse = simplificationService.requestNaverNewsAnalysisV2(naverNewsUrl);
		return new ApiResponse<>(simplificationInfoResponse);
	}
}
