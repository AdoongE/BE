package com.adoonge.seedzip.summary.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.summary.dto.request.ChatGPTRequest;
import com.adoonge.seedzip.summary.dto.response.ChatGPTResponse;
import com.adoonge.seedzip.summary.dto.response.SummaryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.url}")
    private String apiUrl;

    private final RestTemplate template;

    public SummaryResponse requestTextAnalysis(String requestText) throws IOException {
        ChatGPTRequest request = ChatGPTRequest.createYoutubeRequest(apiModel, 500, requestText);

        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, request, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseSummaryResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    public SummaryResponse requestImageAnalysis(String imageUrl) throws IOException {
//        String base64Image = Base64.encodeBase64String(image.getBytes());
//        String imageUrl = "data:image/jpeg;base64," + base64Image;
        ChatGPTRequest request = ChatGPTRequest.createImageRequest(apiModel, 500, "user", imageUrl);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiUrl, request, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseSummaryResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    private SummaryResponse parseSummaryResponse(String response) throws JsonProcessingException {
        // ObjectMapper를 사용한 JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, SummaryResponse.class);
    }
}
