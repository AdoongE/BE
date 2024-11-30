package com.adoonge.seedzip.summary.controller;

import com.adoonge.seedzip.summary.dto.response.ChatGPTResponse;
import com.adoonge.seedzip.summary.service.SummaryService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/image")
    public String imageAnalysis(@RequestParam String imageUrl, @RequestParam String requestText) throws IOException {
        ChatGPTResponse response = summaryService.requestImageAnalysis(imageUrl, requestText);
        return response.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/text")
    public String textAnalysis(@RequestParam String requestText) {
        ChatGPTResponse response = summaryService.requestTextAnalysis(requestText);
        return response.getChoices().get(0).getMessage().getContent();
    }
}
