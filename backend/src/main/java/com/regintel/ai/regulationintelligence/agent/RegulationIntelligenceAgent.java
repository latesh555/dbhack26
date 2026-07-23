package com.regintel.ai.regulationintelligence.agent;

import com.regintel.ai.common.exception.BusinessException;
import com.regintel.ai.llm.exception.LlmException;
import com.regintel.ai.llm.service.LlmService;
import com.regintel.ai.llm.support.LlmJsonParser;
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
    private final LlmJsonParser llmJsonParser;
    private final Validator validator;

    public RegulatoryIntelligence analyze(Regulation regulation) {
        if (regulation.getRawContent() == null || regulation.getRawContent().isBlank()) {
            throw new BusinessException("Regulation has no content to analyze. Upload a document or provide rawContent.");
        }
        if (!llmService.isEnabled() || !llmService.hasAvailableProvider()) {
            throw new LlmException(
                    "LLM provider required for regulation intelligence. Set GROQ_API_KEY or GEMINI_API_KEY, or run Ollama.");
        }

        String json = llmService.completeJson(
                RegulationIntelligencePromptBuilder.systemPrompt(),
                RegulationIntelligencePromptBuilder.userPrompt(regulation));

        RegulatoryIntelligence intelligence = llmJsonParser.parse(json, RegulatoryIntelligence.class);
        enrichFromRegulation(intelligence, regulation);
        validateIntelligence(intelligence);

        log.info("LLM regulatory intelligence analysis completed for regulation {}", regulation.getId());
        return intelligence;
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
            throw new LlmException("LLM regulation intelligence output failed validation: " + message);
        }
    }
}
