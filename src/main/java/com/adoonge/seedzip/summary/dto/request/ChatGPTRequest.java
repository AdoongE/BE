package com.adoonge.seedzip.summary.dto.request;

import com.adoonge.seedzip.summary.dto.Message;
import com.adoonge.seedzip.summary.dto.TextMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGPTRequest {
    @JsonProperty("model")
    private String model;
    @JsonProperty("messages")
    private List<Message> messages;
    @JsonProperty("max_tokens")
    private int maxTokens;

    public static ChatGPTRequest createImageRequest(String model, int maxTokens, String role, String requestText, String imageUrl) {
        TextContent textContent = new TextContent("text", requestText);
        ImageContent imageContent = new ImageContent("image_url", new ImageUrl(imageUrl));
        Message message = new ImageMessage(role, List.of(textContent, imageContent));
        return createChatGPTRequest(model, maxTokens, Collections.singletonList(message));
    }

    public static ChatGPTRequest createTextRequest(String model, int maxTokens, String requestText) {
        Message systemMessage = new TextMessage("system", "당신은 유튜브 영상에 대한 해시태그를 생성하는 유용한 어시스턴트입니다.");
        Message userMessage = new TextMessage("user", "다음 내용만 가지고 어울리는 해시태그 3개, 제목, 요약을 추천해 줘.\n"+requestText);

        return createChatGPTRequest(model, maxTokens, List.of(systemMessage,userMessage));
    }

    private static ChatGPTRequest createChatGPTRequest(String model, int maxTokens, List<Message> messages) {
        return ChatGPTRequest.builder()
                .model(model)
                .maxTokens(maxTokens)
                .messages(messages)
                .build();
    }

}
