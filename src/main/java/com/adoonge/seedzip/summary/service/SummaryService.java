package com.adoonge.seedzip.summary.service;

import com.adoonge.seedzip.summary.dto.request.ChatGPTRequest;
import com.adoonge.seedzip.summary.dto.response.ChatGPTResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SummaryService {

	@Value("${openai.model}")
	private String apiModel;

	@Value("${openai.url}")
	private String apiUrl;

	private final RestTemplate template;

	public ChatGPTResponse requestYoutubeAnalysis(String requestYoutube) {
		ChatGPTRequest request = ChatGPTRequest.createTextRequest(apiModel, 500, requestYoutube);
		return template.postForObject(apiUrl, request, ChatGPTResponse.class);

	}

	public ChatGPTResponse requestImageAnalysis(String imageUrl, String requestText) throws IOException {
		//        String base64Image = Base64.encodeBase64String(image.getBytes());
		//        String imageUrl = "data:image/jpeg;base64," + base64Image;
		ChatGPTRequest request = ChatGPTRequest.createImageRequest(apiModel, 500, "user", requestText, imageUrl);
		return template.postForObject(apiUrl, request, ChatGPTResponse.class);
	}
}
