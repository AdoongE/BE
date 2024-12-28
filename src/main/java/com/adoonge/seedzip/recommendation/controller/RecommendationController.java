package com.adoonge.seedzip.recommendation.controller;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.recommendation.dto.response.RecommendationResponse;
import com.adoonge.seedzip.recommendation.service.RecommendationService;
import com.adoonge.seedzip.recommendation.service.YouTubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/recommendation")
@RequiredArgsConstructor
@Tag(name = "RecommendationController", description = "제목, 요약, 태그 추천 관련 API")
public class RecommendationController {

    private final RecommendationService recommendationService;

    private final YouTubeService youTubeService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 추천 관련 API", description = "이미지 링크를 넣으면 제목, 요약, 태그를 추천해주는 API입니다.")
    public ApiResponse<RecommendationResponse> imageAnalysis(@Parameter(description = "업로드할 이미지", content = @Content(mediaType = "application/octet-stream"))
                                                                 @RequestParam(value = "file", required = false) MultipartFile file) {
        RecommendationResponse response = recommendationService.requestImageAnalysis(file);
        return new ApiResponse<>(response);
    }

    @PostMapping("/youtube")
    @Operation(summary = "유튜브 추천 관련 API", description = "유튜브 링크를 넣으면 제목, 요약, 태그를 추천해주는 API입니다.")
    public ApiResponse<RecommendationResponse> youtubeAnalysis(@RequestParam String youtubeUrl) throws IOException {
        String youtubeData = youTubeService.searchVideos(youtubeUrl);   // 유튜브 데이터 조회
        RecommendationResponse response = recommendationService.requestTextAnalysis(youtubeData); // 요약 데이터 조회
        return new ApiResponse<>(response);
    }

    @PostMapping(value = "/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "PDF 추천 관련 API", description = "PDF 파일을 넣으면 제목, 요약, 태그를 추천해주는 API입니다.")
    public ApiResponse<RecommendationResponse> pdfAnalysis(@Parameter(description = "업로드할 pdf", content = @Content(mediaType = "application/octet-stream"))
    @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        RecommendationResponse recommendationResponse = recommendationService.requestPdfAnalysis(file);
        return new ApiResponse<>(recommendationResponse);
    }
}
