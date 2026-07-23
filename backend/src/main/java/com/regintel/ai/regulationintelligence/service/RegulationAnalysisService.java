package com.regintel.ai.regulationintelligence.service;

import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.service.RegulationService;
import com.regintel.ai.regulationintelligence.dto.RegulationAnalysisDto;
import com.regintel.ai.regulationintelligence.entity.AnalysisStatus;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import com.regintel.ai.regulationintelligence.entity.RiskLevel;
import com.regintel.ai.regulationintelligence.repository.RegulationAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegulationAnalysisService {

    private final RegulationAnalysisRepository analysisRepository;
    private final RegulationService regulationService;

    @Transactional
    public RegulationAnalysisDto.Response create(UUID regulationId, RegulationAnalysisDto.Request request) {
        log.info("Creating regulation analysis for regulation: {}", regulationId);
        Regulation regulation = regulationService.getEntity(regulationId);
        RegulationAnalysis analysis = RegulationAnalysis.builder()
                .regulation(regulation)
                .summary(request.getSummary())
                .keyRequirements(request.getKeyRequirements())
                .complianceAreas(request.getComplianceAreas())
                .riskLevel(request.getRiskLevel() != null ? request.getRiskLevel() : RiskLevel.MEDIUM)
                .status(request.getStatus() != null ? request.getStatus() : AnalysisStatus.PENDING)
                .build();
        return toResponse(analysisRepository.save(analysis));
    }

    @Transactional(readOnly = true)
    public List<RegulationAnalysisDto.Response> findByRegulationId(UUID regulationId) {
        regulationService.getEntity(regulationId);
        return analysisRepository.findByRegulation_Id(regulationId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegulationAnalysisDto.Response findById(UUID id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public RegulationAnalysisDto.Response update(UUID id, RegulationAnalysisDto.Request request) {
        log.info("Updating regulation analysis: {}", id);
        RegulationAnalysis analysis = getEntity(id);
        if (request.getSummary() != null) {
            analysis.setSummary(request.getSummary());
        }
        if (request.getKeyRequirements() != null) {
            analysis.setKeyRequirements(request.getKeyRequirements());
        }
        if (request.getComplianceAreas() != null) {
            analysis.setComplianceAreas(request.getComplianceAreas());
        }
        if (request.getRiskLevel() != null) {
            analysis.setRiskLevel(request.getRiskLevel());
        }
        if (request.getStatus() != null) {
            analysis.setStatus(request.getStatus());
            if (request.getStatus() == AnalysisStatus.COMPLETED) {
                analysis.setAnalyzedAt(LocalDateTime.now());
            }
        }
        return toResponse(analysisRepository.save(analysis));
    }

    public RegulationAnalysis getEntity(UUID id) {
        return analysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RegulationAnalysis", id));
    }

    private RegulationAnalysisDto.Response toResponse(RegulationAnalysis analysis) {
        return RegulationAnalysisDto.Response.builder()
                .id(analysis.getId())
                .regulationId(analysis.getRegulation().getId())
                .summary(analysis.getSummary())
                .keyRequirements(analysis.getKeyRequirements())
                .complianceAreas(analysis.getComplianceAreas())
                .riskLevel(analysis.getRiskLevel())
                .status(analysis.getStatus())
                .analyzedAt(analysis.getAnalyzedAt())
                .createdAt(analysis.getCreatedAt())
                .updatedAt(analysis.getUpdatedAt())
                .build();
    }
}
