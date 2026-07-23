package com.regintel.ai.llm.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.regintel.ai.llm.config.LlmProperties;
import com.regintel.ai.llm.exception.LlmException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
public class GeminiLlmClient implements LlmClient {

    private final LlmProperties.Gemini config;
    private final RestClient restClient;

    public GeminiLlmClient(LlmProperties.Gemini config) {
        this.config = config;
        String baseUrl = config.getBaseUrl().endsWith("/")
                ? config.getBaseUrl().substring(0, config.getBaseUrl().length() - 1)
                : config.getBaseUrl();
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String getProviderName() {
        return "gemini";
    }

    @Override
    public boolean isAvailable() {
        return config.getApiKey() != null && !config.getApiKey().isBlank();
    }

    @Override
    public String completeJson(String systemPrompt, String userPrompt) {
        GeminiRequest request = new GeminiRequest();
        request.setSystemInstruction(new SystemInstruction(
                List.of(new ContentPart(systemPrompt))
        ));
        request.setContents(List.of(new Content("user", List.of(new ContentPart(userPrompt)))));
        request.setGenerationConfig(Map.of(
                "temperature", 0.1,
                "responseMimeType", "application/json"
        ));

        try {
            GeminiResponse response = restClient.post()
                    .uri("/models/{model}:generateContent?key={apiKey}", config.getModel(), config.getApiKey())
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
                throw new LlmException("Gemini returned an empty response");
            }
            Candidate candidate = response.getCandidates().getFirst();
            if (candidate.getContent() == null
                    || candidate.getContent().getParts() == null
                    || candidate.getContent().getParts().isEmpty()) {
                throw new LlmException("Gemini returned empty content");
            }
            String text = candidate.getContent().getParts().getFirst().getText();
            if (text == null || text.isBlank()) {
                throw new LlmException("Gemini returned blank text");
            }
            return text.trim();
        } catch (RestClientResponseException ex) {
            log.error("Gemini API error: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new LlmException("Gemini API call failed: " + ex.getMessage(), ex);
        } catch (LlmException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new LlmException("Gemini API call failed: " + ex.getMessage(), ex);
        }
    }

    @Data
    private static class GeminiRequest {
        private SystemInstruction systemInstruction;
        private List<Content> contents;
        private Map<String, Object> generationConfig;
    }

    @Data
    private static class SystemInstruction {
        private List<ContentPart> parts;

        SystemInstruction(List<ContentPart> parts) {
            this.parts = parts;
        }
    }

    @Data
    private static class Content {
        private String role;
        private List<ContentPart> parts;

        Content(String role, List<ContentPart> parts) {
            this.role = role;
            this.parts = parts;
        }
    }

    @Data
    private static class ContentPart {
        private String text;

        ContentPart(String text) {
            this.text = text;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GeminiResponse {
        private List<Candidate> candidates;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Candidate {
        private Content content;
    }
}
