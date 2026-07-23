package com.regintel.ai.regulationintelligence.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.common.exception.BusinessException;
import com.regintel.ai.regulationintelligence.entity.AnalysisStatus;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import com.regintel.ai.regulationintelligence.repository.RegulationAnalysisRepository;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegulatoryIntelligenceReader {

    private final RegulationAnalysisRepository analysisRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Transactional(readOnly = true)
    public RegulationAnalysis getCompletedAnalysisForRegulation(UUID regulationId) {
        return analysisRepository
                .findFirstByRegulation_IdAndStatusOrderByAnalyzedAtDesc(regulationId, AnalysisStatus.COMPLETED)
                .orElseThrow(() -> new BusinessException(
                        "No completed regulation analysis found for regulation: " + regulationId));
    }

    @Transactional(readOnly = true)
    public RegulatoryIntelligence readStructuredIntelligence(UUID regulationId) {
        RegulationAnalysis analysis = getCompletedAnalysisForRegulation(regulationId);
        return parseIntelligencePayload(analysis);
    }

    @Transactional(readOnly = true)
    public RegulatoryIntelligence parseIntelligencePayload(RegulationAnalysis analysis) {
        if (analysis.getIntelligencePayload() == null || analysis.getIntelligencePayload().isBlank()) {
            throw new BusinessException(
                    "Regulation analysis " + analysis.getId() + " has no structured intelligence payload");
        }
        try {
            RegulatoryIntelligence intelligence = objectMapper.readValue(
                    analysis.getIntelligencePayload(), RegulatoryIntelligence.class);
            validateIntelligence(intelligence);
            return intelligence;
        } catch (JsonProcessingException ex) {
            log.error("Failed to parse regulatory intelligence payload for analysis {}", analysis.getId(), ex);
            throw new BusinessException("Invalid regulatory intelligence JSON payload");
        }
    }

    private void validateIntelligence(RegulatoryIntelligence intelligence) {
        Set<ConstraintViolation<RegulatoryIntelligence>> violations = validator.validate(intelligence);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            throw new BusinessException("Regulatory intelligence validation failed: " + message);
        }
    }
}
