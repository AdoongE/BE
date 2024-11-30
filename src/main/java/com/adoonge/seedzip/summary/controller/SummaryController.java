package com.adoonge.seedzip.summary.controller;

import com.adoonge.seedzip.summary.dto.response.ChatGPTResponse;
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
@Slf4j
public class SummaryController {

	private final SummaryService summaryService;
	private final YouTubeService youTubeService;

	@PostMapping("/image")
	public String imageAnalysis(@RequestParam String imageUrl, @RequestParam String requestText) throws IOException {
		ChatGPTResponse response = summaryService.requestImageAnalysis(imageUrl, requestText);
		return response.getChoices().get(0).getMessage().getContent();
	}

	@PostMapping("/youtube")
	public String youtubeAnalysis(@RequestParam String youtubeUrl) throws IOException {

		String videoData = youTubeService.searchVideos(youtubeUrl);
		log.info(videoData);

		ChatGPTResponse response = summaryService.requestYoutubeAnalysis(videoData);
		log.info(response.toString());
		return response.getChoices().get(0).getMessage().getContent();
	}

