package com.regintel.ai.enterpriseimpact.service;

import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.enterpriseimpact.dto.ImpactAssessmentDto;
import com.regintel.ai.enterpriseimpact.entity.ImpactAssessment;
import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import com.regintel.ai.enterpriseimpact.entity.ImpactStatus;
import com.regintel.ai.enterpriseimpact.repository.ImpactAssessmentRepository;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import com.regintel.ai.regulationintelligence.service.RegulationAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImpactAssessmentService {

    private final ImpactAssessmentRepository impactRepository;
    private final RegulationAnalysisService analysisService;

    @Transactional
    public ImpactAssessmentDto.Response create(UUID analysisId, ImpactAssessmentDto.Request request) {
        log.info("Creating impact assessment for analysis: {}", analysisId);
        RegulationAnalysis analysis = analysisService.getEntity(analysisId);
        ImpactAssessment impact = ImpactAssessment.builder()
                .regulationAnalysis(analysis)
                .businessUnit(request.getBusinessUnit())
                .impactType(request.getImpactType())
                .severity(request.getSeverity() != null ? request.getSeverity() : ImpactSeverity.MEDIUM)
                .description(request.getDescription())
                .estimatedCost(request.getEstimatedCost())
                .status(request.getStatus() != null ? request.getStatus() : ImpactStatus.DRAFT)
                .build();
        return toResponse(impactRepository.save(impact));
    }

    @Transactional(readOnly = true)
    public List<ImpactAssessmentDto.Response> findByAnalysisId(UUID analysisId) {
        analysisService.getEntity(analysisId);
        return impactRepository.findByRegulationAnalysis_Id(analysisId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ImpactAssessmentDto.Response findById(UUID id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ImpactAssessmentDto.Response update(UUID id, ImpactAssessmentDto.Request request) {
        log.info("Updating impact assessment: {}", id);
        ImpactAssessment impact = getEntity(id);
        impact.setBusinessUnit(request.getBusinessUnit());
        impact.setImpactType(request.getImpactType());
        if (request.getSeverity() != null) {
            impact.setSeverity(request.getSeverity());
        }
        impact.setDescription(request.getDescription());
        impact.setEstimatedCost(request.getEstimatedCost());
        if (request.getStatus() != null) {
            impact.setStatus(request.getStatus());
        }
        return toResponse(impactRepository.save(impact));
    }

    public ImpactAssessment getEntity(UUID id) {
        return impactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ImpactAssessment", id));
    }

    private ImpactAssessmentDto.Response toResponse(ImpactAssessment impact) {
        return ImpactAssessmentDto.Response.builder()
                .id(impact.getId())
                .regulationAnalysisId(impact.getRegulationAnalysis().getId())
                .businessUnit(impact.getBusinessUnit())
                .impactType(impact.getImpactType())
                .severity(impact.getSeverity())
                .description(impact.getDescription())
                .estimatedCost(impact.getEstimatedCost())
                .status(impact.getStatus())
                .createdAt(impact.getCreatedAt())
                .updatedAt(impact.getUpdatedAt())
                .build();
    }
}
