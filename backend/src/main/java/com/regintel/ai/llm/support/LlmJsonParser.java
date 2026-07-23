package com.regintel.ai.llm.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.common.exception.BusinessException;
import com.regintel.ai.llm.exception.LlmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LlmJsonParser {

    private final ObjectMapper objectMapper;

    public <T> T parse(String rawJson, Class<T> type) {
        try {
            ObjectMapper lenient = objectMapper.copy()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return lenient.readValue(sanitize(rawJson), type);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("LLM returned invalid JSON for " + type.getSimpleName()
                    + ": " + ex.getOriginalMessage());
        }
    }

    public String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new LlmException("Failed to serialize context for LLM prompt", ex);
        }
    }

    public String sanitize(String json) {
        if (json == null) {
            throw new BusinessException("LLM returned null response");
        }
        String trimmed = json.trim();
        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf('\n');
            int end = trimmed.lastIndexOf("```");
            if (start >= 0 && end > start) {
                return trimmed.substring(start + 1, end).trim();
            }
        }
        return trimmed;
    }
}
