package com.adoonge.seedzip.recommendation.dto.request;

import com.adoonge.seedzip.recommendation.dto.Message;
import com.adoonge.seedzip.recommendation.dto.TextMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public static ChatGPTRequest createImageRequest(String model, int maxTokens, String imageUrl) {
        Message systemMessage = new TextMessage("system", "You are a helpful assistant for generating hashtags for . Please answer in Korean.");
        TextContent textContent = new TextContent("text", "Analyze the image from the given URL and extract title, a 3-sentence summary, and tags as a comma-separated string without #, then return the result as a raw JSON object without any code block or additional formatting.");
        ImageContent imageContent = new ImageContent("image_url", new ImageUrl(imageUrl));
        Message userMessage = new ImageMessage("user", List.of(textContent, imageContent));
        return createChatGPTRequest(model, maxTokens, List.of(systemMessage, userMessage));
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
