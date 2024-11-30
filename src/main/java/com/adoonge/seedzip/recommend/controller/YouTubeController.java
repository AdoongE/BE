package com.adoonge.seedzip.recommend.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.recommend.service.OpenAiTagService;
import com.adoonge.seedzip.recommend.service.YouTubeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class YouTubeController {

	@Autowired
	private YouTubeService youtubeService;

	@Autowired
	private OpenAiTagService openAiTagService;

	@GetMapping("/get-video-metadata")
	public ApiResponse<String> getVideoMetadata(@RequestParam String videoUrl) throws IOException {

		String videoData = youtubeService.searchVideos(videoUrl);

		log.info(videoData);

		String tags = openAiTagService.generateTags(videoData);

		log.info(tags);

		return new ApiResponse<>(tags); // 태그 데이터 반환
	}

}
