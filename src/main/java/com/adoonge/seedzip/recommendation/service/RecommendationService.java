package com.adoonge.seedzip.recommendation.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.recommendation.dto.request.ChatGPTRequest;
import com.adoonge.seedzip.recommendation.dto.response.ChatGPTResponse;
import com.adoonge.seedzip.recommendation.dto.response.RecommendationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.url}")
    private String apiUrl;

    private final RestTemplate template;

    public RecommendationResponse requestTextAnalysis(String requestText) {
        ChatGPTRequest request = ChatGPTRequest.createYoutubeRequest(apiModel, 500, requestText);

        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, request, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseRecommendationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    public RecommendationResponse requestImageAnalysis(MultipartFile file)  {
        String base64Image;
        try {
            base64Image = Base64.encodeBase64String(file.getBytes());
        } catch (IOException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
        String imageUrl = "data:image/jpeg;base64," + base64Image;
        ChatGPTRequest request = ChatGPTRequest.createImageRequest(apiModel, 500, imageUrl);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiUrl, request, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseRecommendationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    private RecommendationResponse parseRecommendationResponse(String response) throws JsonProcessingException {
        // ObjectMapper를 사용한 JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, RecommendationResponse.class);
    }
}
