package com.regintel.ai.regulationintelligence.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.common.exception.BusinessException;
import com.regintel.ai.llm.config.LlmProperties;
import com.regintel.ai.llm.exception.LlmException;
import com.regintel.ai.llm.service.LlmService;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegulationIntelligenceAgent {

    private final LlmService llmService;
    private final LlmProperties llmProperties;
    private final HeuristicRegulationIntelligenceAnalyzer heuristicAnalyzer;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public RegulatoryIntelligence analyze(Regulation regulation) {
        if (regulation.getRawContent() == null || regulation.getRawContent().isBlank()) {
            throw new BusinessException("Regulation has no content to analyze. Upload a document or provide rawContent.");
        }

        if (llmService.isEnabled() && llmService.hasAvailableProvider()) {
            try {
                return analyzeWithLlm(regulation);
            } catch (Exception ex) {
                log.warn("LLM analysis failed for regulation {}: {}", regulation.getId(), ex.getMessage());
                if (!llmProperties.isFallbackToHeuristics()) {
                    throw ex instanceof BusinessException businessException
                            ? businessException
                            : new BusinessException("LLM analysis failed: " + ex.getMessage());
                }
            }
        } else {
            log.info("No LLM provider available; using heuristic analysis for regulation {}", regulation.getId());
        }

        return heuristicAnalyzer.analyze(regulation);
    }

    private RegulatoryIntelligence analyzeWithLlm(Regulation regulation) {
        String systemPrompt = RegulationIntelligencePromptBuilder.systemPrompt();
        String userPrompt = RegulationIntelligencePromptBuilder.userPrompt(regulation);

        String json = llmService.completeJson(systemPrompt, userPrompt);
        json = sanitizeJson(json);

        try {
            RegulatoryIntelligence intelligence = objectMapper.readValue(json, RegulatoryIntelligence.class);
            enrichFromRegulation(intelligence, regulation);
            validateIntelligence(intelligence);
            log.info("LLM regulatory intelligence analysis completed for regulation {}", regulation.getId());
            return intelligence;
        } catch (JsonProcessingException ex) {
            throw new BusinessException("LLM returned invalid JSON: " + ex.getOriginalMessage());
        }
    }

    private void enrichFromRegulation(RegulatoryIntelligence intelligence, Regulation regulation) {
        if (intelligence.getDocumentType() == null || intelligence.getDocumentType().isBlank()) {
            intelligence.setDocumentType(regulation.getDocumentType());
        }
        if (intelligence.getRegulatoryAuthority() == null || intelligence.getRegulatoryAuthority().isBlank()) {
            intelligence.setRegulatoryAuthority(regulation.getSource());
        }
        if (intelligence.getEffectiveDate() == null) {
            intelligence.setEffectiveDate(regulation.getEffectiveDate());
        }
        if (intelligence.getJurisdictions() == null || intelligence.getJurisdictions().isEmpty()) {
            intelligence.setJurisdictions(java.util.List.of(regulation.getJurisdiction()));
        }
    }

    private void validateIntelligence(RegulatoryIntelligence intelligence) {
        Set<ConstraintViolation<RegulatoryIntelligence>> violations = validator.validate(intelligence);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            throw new LlmException("LLM output failed validation: " + message);
        }
    }

    private String sanitizeJson(String json) {
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
