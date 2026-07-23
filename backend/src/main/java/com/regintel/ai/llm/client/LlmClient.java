package com.regintel.ai.llm.client;

public interface LlmClient {

    String getProviderName();

    boolean isAvailable();

    String completeJson(String systemPrompt, String userPrompt);
}
