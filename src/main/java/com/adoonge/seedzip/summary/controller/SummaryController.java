package com.adoonge.seedzip.summary.controller;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.summary.dto.response.ChatGPTResponse;
import com.adoonge.seedzip.summary.dto.response.SummaryResponse;
import com.adoonge.seedzip.summary.service.SummaryService;
import com.adoonge.seedzip.summary.service.YouTubeService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    private final YouTubeService youTubeService;

    @PostMapping("/image")
    public ApiResponse<SummaryResponse> imageAnalysis(@RequestParam String imageUrl) throws IOException {
        SummaryResponse response = summaryService.requestImageAnalysis(imageUrl);
        return new ApiResponse<>(response);
    }

    @PostMapping("/youtube")
    public ApiResponse<SummaryResponse> youtubeAnalysis(@RequestParam String youtubeUrl) throws IOException {
        String youtubeData = youTubeService.searchVideos(youtubeUrl);   // 유튜브 데이터 조회
        SummaryResponse response = summaryService.requestTextAnalysis(youtubeData); // 요약 데이터 조회
        return new ApiResponse<>(response);
    }
}
