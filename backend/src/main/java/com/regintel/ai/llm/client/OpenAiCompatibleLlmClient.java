package com.regintel.ai.llm.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.regintel.ai.llm.exception.LlmException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.List;

@Slf4j
public class OpenAiCompatibleLlmClient implements LlmClient {

    private final String providerName;
    private final String baseUrl;
    private final String apiKey;
    private final String model;
    private final RestClient restClient;

    public OpenAiCompatibleLlmClient(
            String providerName,
            String baseUrl,
            String apiKey,
            String model,
            Duration timeout) {
        this.providerName = providerName;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.apiKey = apiKey;
        this.model = model;
        RestClient.Builder builder = RestClient.builder()
                .baseUrl(this.baseUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        if (apiKey != null && !apiKey.isBlank()) {
            builder.defaultHeader("Authorization", "Bearer " + apiKey);
        }
        this.restClient = builder.build();
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public boolean isAvailable() {
        if ("ollama".equalsIgnoreCase(providerName)) {
            return true;
        }
        return apiKey != null && !apiKey.isBlank();
    }

    @Override
    public String completeJson(String systemPrompt, String userPrompt) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(model);
        request.setTemperature(0.1);
        request.setMessages(List.of(
                new ChatMessage("system", systemPrompt),
                new ChatMessage("user", userPrompt)
        ));
        request.setResponseFormat(new ResponseFormat("json_object"));

        try {
            ChatCompletionResponse response = restClient.post()
                    .uri("/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(ChatCompletionResponse.class);

            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new LlmException(providerName + " returned an empty response");
            }
            String content = response.getChoices().getFirst().getMessage().getContent();
            if (content == null || content.isBlank()) {
                throw new LlmException(providerName + " returned empty content");
            }
            return content.trim();
        } catch (RestClientResponseException ex) {
            log.error("{} API error: status={}, body={}", providerName, ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new LlmException(providerName + " API call failed: " + ex.getMessage(), ex);
        } catch (LlmException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new LlmException(providerName + " API call failed: " + ex.getMessage(), ex);
        }
    }

    @Data
    private static class ChatCompletionRequest {
        private String model;
        private List<ChatMessage> messages;
        private Double temperature;

        @JsonProperty("response_format")
        private ResponseFormat responseFormat;
    }

    @Data
    private static class ChatMessage {
        private String role;
        private String content;

        ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @Data
    private static class ResponseFormat {
        private String type;

        ResponseFormat(String type) {
            this.type = type;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ChatCompletionResponse {
        private List<Choice> choices;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Choice {
        private ChatMessage message;
    }
}
