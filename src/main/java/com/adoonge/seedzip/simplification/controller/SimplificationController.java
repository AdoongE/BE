package com.adoonge.seedzip.simplification.controller;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.simplification.dto.response.SimplificationResponse;
import com.adoonge.seedzip.simplification.service.SimplificationService;
import com.adoonge.seedzip.simplification.service.YouTubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/simplification")
@RequiredArgsConstructor
@Tag(name = "SimplificationController", description = "제목, 요약, 태그 간략화 관련 API")
public class SimplificationController {

    private final SimplificationService simplificationService;

    private final YouTubeService youTubeService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 간략화 관련 API", description = "이미지를 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
    public ApiResponse<SimplificationResponse> imageAnalysis(@Parameter(description = "업로드할 이미지", content = @Content(mediaType = "application/octet-stream"))
                                                                 @RequestParam(value = "file", required = false) MultipartFile file) {
        SimplificationResponse response = simplificationService.requestImageAnalysis(file);
        return new ApiResponse<>(response);
    }

    @PostMapping("/youtube")
    @Operation(summary = "유튜브 간략화 관련 API", description = "유튜브 링크를 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
    public ApiResponse<SimplificationResponse> youtubeAnalysis(@RequestParam String youtubeUrl) throws IOException {
        String youtubeData = youTubeService.searchVideos(youtubeUrl);   // 유튜브 데이터 조회
        SimplificationResponse response = simplificationService.requestTextAnalysis(youtubeData); // 요약 데이터 조회
        return new ApiResponse<>(response);
    }

    @PostMapping(value = "/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "PDF 간략화 관련 API", description = "PDF 파일을 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
    public ApiResponse<SimplificationResponse> pdfAnalysis(@Parameter(description = "업로드할 pdf", content = @Content(mediaType = "application/octet-stream"))
    @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        SimplificationResponse simplificationResponse = simplificationService.requestPdfAnalysis(file);
        return new ApiResponse<>(simplificationResponse);
    }

    @PostMapping(value = "/naver-news")
    @Operation(summary = "네이버 뉴스 간략화 관련 API", description = "네이버 뉴스 링크를 넣으면 제목, 요약, 태그를 간략화해주는 API입니다.")
    public ApiResponse<SimplificationResponse> naverNewsAnalysis(@RequestParam String naverNewsUrl) throws IOException {
        SimplificationResponse simplificationResponse = simplificationService.requestNaverNewsAnalysis(naverNewsUrl);
        return new ApiResponse<>(simplificationResponse);
    }
}
