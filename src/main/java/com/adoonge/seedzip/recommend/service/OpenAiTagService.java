package com.adoonge.seedzip.recommend.service;

import java.util.List;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAiTagService {

	@Value("${openai.api.key}")
	private String apiKey;

	private final OpenAiService openAiService;

	public OpenAiTagService() {
		try {
			this.openAiService = new OpenAiService(apiKey);
		} catch (Exception e) {
			throw new RuntimeException("Error initializing OpenAiService", e);  // 예외 던짐
		}
	}


	public String generateTags(String content) {
		// GPT 모델을 사용하여 태그를 생성
		List<ChatMessage> messages = List.of(
			new ChatMessage("system", "당신은 유튜브 영상에 대한 해시태그를 생성하는 유용한 어시스턴트입니다."),
			new ChatMessage("user", "다음 내용만 가지고 어울리는 해시태그 3개, 제목, 요약을 추천해 줘.\n"+content)
		);

		// ChatCompletionRequest 객체 설정
		ChatCompletionRequest request = ChatCompletionRequest.builder()
			.model("gpt-4o-mini")  // 사용할 모델 지정 (GPT-3.5)
			.messages(messages)
			.maxTokens(100)  // 결과 길이 설정
			.temperature(0.7)  // 생성된 텍스트의 다양성 조절
			.build();

		// API 호출하여 태그 추천 받기
		var response = openAiService.createChatCompletion(request);

		// GPT-3로부터 받은 태그 응답에서 텍스트 추출
		String generatedTags = response.getChoices().get(0).getMessage().getContent();
		return generatedTags.trim();  // 공백 제거하여 반환
	}
}
