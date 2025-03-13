package com.adoonge.seedzip.simplification.service;

import com.adoonge.seedzip.content.service.S3Service;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.domain.MemberAiUsages;
import com.adoonge.seedzip.member.repository.MemberAiUsageRepository;
import com.adoonge.seedzip.member.repository.MemberRepository;
import com.adoonge.seedzip.simplification.dto.request.ChatGPTRequest;
import com.adoonge.seedzip.simplification.dto.response.ChatGPTResponse;
import com.adoonge.seedzip.simplification.dto.response.SimplificationAllResponse;
import com.adoonge.seedzip.simplification.dto.response.SimplificationInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimplificationService {

    @Value("${openai.model}")
    private String apiModel;

    @Value("${openai.url}")
    private String apiUrl;

    private final RestTemplate template;
    private final NaverNewsService naverNewsService;
    private final S3Service s3Service;

    private final MemberAiUsageRepository memberAiUsageRepository;
    private final MemberRepository memberRepository;

    private final static int DAILY_AI_LIMIT = 5;

    /**
     * 기존 버전
     */
    public SimplificationInfoResponse requestTextAnalysis(String requestText) {
        ChatGPTRequest request = ChatGPTRequest.createYoutubeRequest(apiModel, 500, requestText);

        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, request, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseSimplificationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    public SimplificationInfoResponse requestImageAnalysis(MultipartFile file)  {
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
            return parseSimplificationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    // 네이버 뉴스 분석 요청
    public SimplificationInfoResponse requestNaverNewsAnalysis(String naverNewsUrl) throws IOException {
        if(!naverNewsUrl.contains("https://n.news.naver.com")) {
            throw SeedzipException.from(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 네이버 뉴스 제목 가져오기
        String newsTitle = getNewsTitle(naverNewsUrl);

        String newsDescription = naverNewsService.searchNews(newsTitle);

        ChatGPTRequest newsRequest = ChatGPTRequest.createNewsRequest(apiModel, 500, newsDescription);
        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, newsRequest, ChatGPTResponse.class);
        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseSimplificationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }

    }

    /**
     * PDF 간략화
     */
    public SimplificationInfoResponse requestPdfAnalysis(MultipartFile file) throws IOException {

        ChatGPTRequest pdfRequest = ChatGPTRequest.createPdfRequest(apiModel, 500, extractTextFromPdf(file));

        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, pdfRequest, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseSimplificationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }

    }

    /**
     * s3에 먼저 저장하는 로직
     */

    public SimplificationAllResponse.simplificationLinkResponse requestTextAnalysisV2(String requestText) {
        ChatGPTRequest request = ChatGPTRequest.createYoutubeRequest(apiModel, 500, requestText);

        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, request, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            SimplificationInfoResponse info = parseSimplificationResponse(response);

            return SimplificationAllResponse.simplificationLinkResponse.builder()
                    .simplificationInfo(info)
                    .build();
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    public SimplificationAllResponse.simplificationFileResponse requestImageAnalysisV2(List<MultipartFile> files, int thumbnailIdx)  {

        List<String> fileUrls = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        MultipartFile thumbnail = files.get(thumbnailIdx);

        files.stream().forEach(file -> {
            try {
                // S3에 파일 업로드 및 URL 가져오기
                String fileUrl = s3Service.uploadImgFile(file);
                fileUrls.add(fileUrl);
                fileNames.add(file.getOriginalFilename());

            } catch (IOException e) {
                throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
            }
        });
        SimplificationInfoResponse info = processThumbnailImage(thumbnail);
        return SimplificationAllResponse.simplificationFileResponse.builder()
                .files(fileUrls)
                .fileNames(fileNames)
                .simplificationInfo(info)
                .build();
    }

    // 네이버 뉴스 분석 요청
    public SimplificationAllResponse.simplificationLinkResponse requestNaverNewsAnalysisV2(String naverNewsUrl) throws IOException {
        if(!naverNewsUrl.contains("https://n.news.naver.com")) {
            throw SeedzipException.from(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 네이버 뉴스 제목 가져오기
        String newsTitle = getNewsTitle(naverNewsUrl);

        String newsDescription = naverNewsService.searchNews(newsTitle);

        ChatGPTRequest newsRequest = ChatGPTRequest.createNewsRequest(apiModel, 500, newsDescription);
        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, newsRequest, ChatGPTResponse.class);
        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            SimplificationInfoResponse info = parseSimplificationResponse(response);

            return SimplificationAllResponse.simplificationLinkResponse.builder()
                    .simplificationInfo(info)
                    .link(naverNewsUrl)
                    .build();
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }

    }

    /**
     * PDF 간략화
     */
    public SimplificationAllResponse.simplificationFileResponse requestPdfAnalysisV2(List<MultipartFile> files, int thumbnailIdx) throws IOException {

        List<String> fileUrls = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        MultipartFile thumbnail = files.get(thumbnailIdx);

        files.stream().forEach(file -> {
            try {
                // S3에 파일 업로드 및 URL 가져오기
                String fileUrl = s3Service.uploadDocFile(file);
                fileUrls.add(fileUrl);
                fileNames.add(file.getOriginalFilename());

            } catch (IOException e) {
                e.printStackTrace();
                throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
            }
        });
        SimplificationInfoResponse info = processThumbnailPdf(thumbnail);
        return SimplificationAllResponse.simplificationFileResponse.builder()
                .files(fileUrls)
                .fileNames(fileNames)
                .simplificationInfo(info)
                .build();

    }

    private SimplificationInfoResponse parseSimplificationResponse(String response) throws JsonProcessingException {
        // ObjectMapper를 사용한 JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, SimplificationInfoResponse.class);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = null;
        try (InputStream inputStream = file.getInputStream()) {
            document = PDDocument.load(inputStream);

            // PDFBox를 사용한 PDF 텍스트 추출
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            return pdfTextStripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        } finally {
            if (document != null) {
                document.close(); // 리소스 해제
            }
        }
    }

    private String getNewsTitle(String naverNewsUrl) throws IOException {
        Document document = Jsoup.connect(naverNewsUrl).get();
        return document.title();
    }

    private SimplificationInfoResponse processThumbnailImage(MultipartFile file) {
        try {
            String base64Image = Base64.encodeBase64String(file.getBytes());
            String imageUrl = "data:image/jpeg;base64," + base64Image;

            ChatGPTRequest request = ChatGPTRequest.createImageRequest(apiModel, 500, imageUrl);
            ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, request, ChatGPTResponse.class);

            String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();
            return parseSimplificationResponse(response);
        } catch (Exception e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    private SimplificationInfoResponse processThumbnailPdf(MultipartFile file) throws IOException {
        ChatGPTRequest pdfRequest = ChatGPTRequest.createPdfRequest(apiModel, 500, extractTextFromPdf(file));

        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, pdfRequest, ChatGPTResponse.class);

        String response = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        try{
            return parseSimplificationResponse(response);
        } catch (JsonProcessingException e) {
            throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
        }
    }

    public void checkDailyAiUsage(Member member) {
        LocalDate today = LocalDate.now();
        MemberAiUsages memberAiUsages = memberAiUsageRepository.findByMemberId(member.getId())
            .orElseGet(() -> createMemberAiUsage(member, today));

        if(memberAiUsages.getUsageCount() > DAILY_AI_LIMIT) {
            throw SeedzipException.from(ErrorCode.DAILY_AI_LIMIT_EXCEEDED);
        }

        memberAiUsages.increaseUsageCount();
        memberAiUsageRepository.save(memberAiUsages);
    }

    private MemberAiUsages createMemberAiUsage(Member member, LocalDate today) {
        return MemberAiUsages.builder()
            .member(member)
            .usageCount(0L)
            .lastUsedDate(today)
            .build();
    }

    @Scheduled(cron = "0 0 0 * * ?")    // 매일 0시에 실행
    public void resetDailyAiUsage() {
        LocalDate today = LocalDate.now();
        memberAiUsageRepository.deleteByLastUsedDateBefore(today);
    }


}
