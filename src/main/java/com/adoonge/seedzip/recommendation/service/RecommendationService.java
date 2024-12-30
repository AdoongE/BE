package com.adoonge.seedzip.recommendation.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.recommendation.dto.request.ChatGPTRequest;
import com.adoonge.seedzip.recommendation.dto.response.ChatGPTResponse;
import com.adoonge.seedzip.recommendation.dto.response.RecommendationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

import jdk.jfr.consumer.RecordedObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    private final NaverNewsService naverNewsService;

    public RecommendationResponse requestTextAnalysis(String requestText) {
        if(!requestText.contains("https://www.youtube.com/watch?v=")){
            throw SeedzipException.from(ErrorCode.INVALID_INPUT_VALUE);
        }

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

    // 네이버 뉴스 분석 요청
    public RecommendationResponse requestNaverNewsAnalysis(String naverNewsUrl) throws IOException {
        if(!naverNewsUrl.contains("https://n.news.naver.com")) {
            throw SeedzipException.from(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 네이버 뉴스 제목 가져오기
        String newsTitle = getNewsTitle(naverNewsUrl);

        String newsDescription = naverNewsService.searchNews(newsTitle);
        log.info(newsDescription);

        ChatGPTRequest newsRequest = ChatGPTRequest.createNewsRequest(apiModel, 500, newsDescription);
        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, newsRequest, ChatGPTResponse.class);
        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseRecommendationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }

    }

    public RecommendationResponse requestPdfAnalysis(MultipartFile file) throws IOException {
        ChatGPTRequest pdfRequest = ChatGPTRequest.createPdfRequest(apiModel, 500, extractTextFromPdf(file));

        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, pdfRequest, ChatGPTResponse.class);

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

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            PDDocument document = PDDocument.load(inputStream);

            // PDFBox를 사용한 PDF 텍스트 추출
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            return pdfTextStripper.getText(document);
        } catch (IOException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }

    }

    private String getNewsTitle(String naverNewsUrl) throws IOException {
        Document document = Jsoup.connect(naverNewsUrl).get();
        return document.title();
    }
}
