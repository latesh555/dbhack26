package com.regintel.ai.regulationintelligence.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulationintelligence.agent.RegulationIntelligenceAgent;
import com.regintel.ai.regulationintelligence.entity.AnalysisStatus;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import com.regintel.ai.regulationintelligence.entity.RiskLevel;
import com.regintel.ai.regulationintelligence.repository.RegulationAnalysisRepository;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegulationIntelligenceAgentService {

    private final RegulationIntelligenceAgent intelligenceAgent;
    private final RegulationAnalysisRepository analysisRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public RegulationAnalysis analyzeAndPersist(Regulation regulation) throws JsonProcessingException {
        log.info("Running regulation intelligence agent for regulation: {}", regulation.getId());
        RegulatoryIntelligence intelligence = intelligenceAgent.analyze(regulation);

        RegulationAnalysis analysis = RegulationAnalysis.builder()
                .regulation(regulation)
                .summary(intelligence.getExecutiveSummary())
                .keyRequirements(intelligence.getNewRequirements().stream()
                        .map(r -> r.getTitle())
                        .reduce((a, b) -> a + "; " + b)
                        .orElse(""))
                .complianceAreas(String.join(", ", intelligence.getBusinessDomains()))
                .riskLevel(mapSeverity(intelligence.getSeverity()))
                .status(AnalysisStatus.COMPLETED)
                .analyzedAt(LocalDateTime.now())
                .intelligencePayload(objectMapper.writeValueAsString(intelligence))
                .build();

        return analysisRepository.save(analysis);
    }

    private RiskLevel mapSeverity(String severity) {
        if (severity == null) {
            return RiskLevel.MEDIUM;
        }
        return switch (severity.toUpperCase()) {
            case "CRITICAL" -> RiskLevel.CRITICAL;
            case "HIGH" -> RiskLevel.HIGH;
            case "LOW" -> RiskLevel.LOW;
            default -> RiskLevel.MEDIUM;
        };
    }
}
