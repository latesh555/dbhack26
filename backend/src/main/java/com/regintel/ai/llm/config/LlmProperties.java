package com.regintel.ai.llm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "regintel.llm")
public class LlmProperties {

    private boolean enabled = true;
    private List<String> providers = new ArrayList<>(List.of("groq", "gemini", "ollama"));
    private int timeoutSeconds = 120;
    private boolean fallbackToHeuristics = true;
    private Groq groq = new Groq();
    private Gemini gemini = new Gemini();
    private Ollama ollama = new Ollama();

    @Data
    public static class Groq {
        private String baseUrl = "https://api.groq.com/openai/v1";
        private String apiKey = "";
        private String model = "llama-3.3-70b-versatile";
    }

    @Data
    public static class Gemini {
        private String baseUrl = "https://generativelanguage.googleapis.com/v1beta";
        private String apiKey = "";
        private String model = "gemini-2.0-flash";
    }

    @Data
    public static class Ollama {
        private String baseUrl = "http://localhost:11434/v1";
        private String model = "llama3.2";
    }
}
