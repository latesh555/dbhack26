package com.regintel.ai.llm.service;

import com.regintel.ai.llm.client.GeminiLlmClient;
import com.regintel.ai.llm.client.LlmClient;
import com.regintel.ai.llm.client.OpenAiCompatibleLlmClient;
import com.regintel.ai.llm.config.LlmProperties;
import com.regintel.ai.llm.exception.LlmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LlmService {

    private final LlmProperties properties;
    private final List<LlmClient> clients;

    public LlmService(LlmProperties properties) {
        this.properties = properties;
        this.clients = buildClients(properties);
    }

    public boolean isEnabled() {
        return properties.isEnabled();
    }

    public boolean hasAvailableProvider() {
        return clients.stream().anyMatch(LlmClient::isAvailable);
    }

    public String completeJson(String systemPrompt, String userPrompt) {
        if (!properties.isEnabled()) {
            throw new LlmException("LLM integration is disabled");
        }

        List<LlmClient> available = clients.stream().filter(LlmClient::isAvailable).toList();
        if (available.isEmpty()) {
            throw new LlmException("No LLM provider is configured. Set GROQ_API_KEY or GEMINI_API_KEY, or run Ollama locally.");
        }

        LlmException lastError = null;
        for (LlmClient client : available) {
            try {
                log.info("Calling LLM provider: {}", client.getProviderName());
                return client.completeJson(systemPrompt, userPrompt);
            } catch (LlmException ex) {
                lastError = ex;
                log.warn("LLM provider {} failed: {}", client.getProviderName(), ex.getMessage());
            }
        }

        throw lastError != null ? lastError : new LlmException("All LLM providers failed");
    }

    private static List<LlmClient> buildClients(LlmProperties properties) {
        Duration timeout = Duration.ofSeconds(properties.getTimeoutSeconds());
        List<LlmClient> result = new ArrayList<>();

        for (String provider : properties.getProviders()) {
            switch (provider.trim().toLowerCase()) {
                case "groq" -> result.add(new OpenAiCompatibleLlmClient(
                        "groq",
                        properties.getGroq().getBaseUrl(),
                        properties.getGroq().getApiKey(),
                        properties.getGroq().getModel(),
                        timeout));
                case "gemini" -> result.add(new GeminiLlmClient(properties.getGemini()));
                case "ollama" -> result.add(new OpenAiCompatibleLlmClient(
                        "ollama",
                        properties.getOllama().getBaseUrl(),
                        "",
                        properties.getOllama().getModel(),
                        timeout));
                default -> log.warn("Unknown LLM provider configured: {}", provider);
            }
        }
        return result;
    }
}
