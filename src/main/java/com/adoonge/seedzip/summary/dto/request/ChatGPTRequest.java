package com.adoonge.seedzip.summary.dto.request;

import com.adoonge.seedzip.summary.dto.Message;
import com.adoonge.seedzip.summary.dto.TextMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public static ChatGPTRequest createImageRequest(String model, int maxTokens, String role, String imageUrl) {
        String prompt = "Please answer in Korean. Analyze the image from the given URL and extract title, a 3-sentence summary, and tags as a comma-separated string without #, then return the result as a raw JSON object without any code block or additional formatting.";
        TextContent textContent = new TextContent("text", prompt);
        ImageContent imageContent = new ImageContent("image_url", new ImageUrl(imageUrl));
        Message message = new ImageMessage(role, List.of(textContent, imageContent));
        return createChatGPTRequest(model, maxTokens, Collections.singletonList(message));
    }


    public static ChatGPTRequest createYoutubeRequest(String model, int maxTokens, String requestText) {
        Message systemMessage = new TextMessage("system", "You are a helpful assistant for generating hashtags for YouTube videos. Please answer in Korean.");
        Message userMessage = new TextMessage("user", "Analyze the given text about a YouTube video, suggest a title, provide a 3-sentence summary, and 3 tags as a comma-separated string without #. Return the result as a raw JSON object without any code block or additional formatting.\n" + requestText);
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
