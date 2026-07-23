package com.regintel.ai.enterpriseimpact.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.common.exception.BusinessException;
import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.enterpriseimpact.agent.EnterpriseImpactAnalystAgent;
import com.regintel.ai.enterpriseimpact.entity.*;
import com.regintel.ai.enterpriseimpact.repository.EnterpriseImpactAssessmentRepository;
import com.regintel.ai.enterpriseimpact.schema.*;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.service.RegulationService;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import com.regintel.ai.regulationintelligence.service.RegulatoryIntelligenceReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseImpactAnalystService {

    private final EnterpriseImpactAnalystAgent analystAgent;
    private final RegulatoryIntelligenceReader intelligenceReader;
    private final RegulationService regulationService;
    private final EnterpriseImpactAssessmentRepository assessmentRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public EnterpriseImpactAssessmentSchema startAnalysis(UUID regulationId) {
        log.info("Starting enterprise impact analysis for regulation: {}", regulationId);
        Regulation regulation = regulationService.getEntity(regulationId);
        RegulationAnalysis analysis = intelligenceReader.getCompletedAnalysisForRegulation(regulationId);
        RegulatoryIntelligence intelligence = intelligenceReader.parseIntelligencePayload(analysis);

        EnterpriseImpactAssessment assessment = EnterpriseImpactAssessment.builder()
                .regulation(regulation)
                .regulationAnalysis(analysis)
                .status(EnterpriseAssessmentStatus.RUNNING)
                .build();
        assessment = assessmentRepository.save(assessment);

        try {
            EnterpriseImpactAssessmentSchema result = analystAgent.analyze(intelligence);
            persistResult(assessment, result);
            assessment.setStatus(EnterpriseAssessmentStatus.COMPLETED);
            assessment.setAnalyzedAt(LocalDateTime.now());
            assessmentRepository.save(assessment);
            return toSchema(assessment, result);
        } catch (Exception ex) {
            log.error("Enterprise impact analysis failed for regulation {}", regulationId, ex);
            assessment.setStatus(EnterpriseAssessmentStatus.FAILED);
            assessment.setErrorMessage(ex.getMessage());
            assessmentRepository.save(assessment);
            throw new BusinessException("Enterprise impact analysis failed: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public EnterpriseImpactAssessmentSchema getByRegulationId(UUID regulationId) {
        EnterpriseImpactAssessment assessment = assessmentRepository
                .findFirstByRegulation_IdAndStatusOrderByAnalyzedAtDesc(
                        regulationId, EnterpriseAssessmentStatus.COMPLETED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enterprise impact assessment for regulation", regulationId));
        return toSchema(assessment);
    }

    @Transactional(readOnly = true)
    public List<ImpactItemSchema> getImpactedApplications(UUID regulationId) {
        return getItemsByCategory(regulationId, ImpactCategory.APPLICATION);
    }

    @Transactional(readOnly = true)
    public List<ImpactItemSchema> getImpactedCustomers(UUID regulationId) {
        return getItemsByCategory(regulationId, ImpactCategory.CUSTOMER);
    }

    @Transactional(readOnly = true)
    public List<ImpactItemSchema> getImpactedTransactions(UUID regulationId) {
        return getItemsByCategory(regulationId, ImpactCategory.TRANSACTION);
    }

    @Transactional(readOnly = true)
    public RiskHeatmapSchema getRiskHeatmap(UUID regulationId) {
        EnterpriseImpactAssessment assessment = getCompletedAssessment(regulationId);
        if (assessment.getRiskHeatmap() == null) {
            throw new BusinessException("Risk heatmap not available for regulation: " + regulationId);
        }
        try {
            return objectMapper.readValue(assessment.getRiskHeatmap(), RiskHeatmapSchema.class);
        } catch (JsonProcessingException ex) {
            throw new BusinessException("Failed to read risk heatmap data");
        }
    }

    private List<ImpactItemSchema> getItemsByCategory(UUID regulationId, ImpactCategory category) {
        EnterpriseImpactAssessment assessment = getCompletedAssessment(regulationId);
        return assessment.getImpactItems().stream()
                .filter(item -> item.getCategory() == category)
                .map(this::toItemSchema)
                .toList();
    }

    private EnterpriseImpactAssessment getCompletedAssessment(UUID regulationId) {
        return assessmentRepository
                .findFirstByRegulation_IdAndStatusOrderByAnalyzedAtDesc(
                        regulationId, EnterpriseAssessmentStatus.COMPLETED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enterprise impact assessment for regulation", regulationId));
    }

    private void persistResult(EnterpriseImpactAssessment assessment, EnterpriseImpactAssessmentSchema result)
            throws JsonProcessingException {
        assessment.setComplianceRiskSummary(objectMapper.writeValueAsString(result.getComplianceRisk()));
        assessment.setRiskHeatmap(objectMapper.writeValueAsString(result.getRiskHeatmap()));

        List<EnterpriseImpactItem> items = new ArrayList<>();
        items.addAll(mapItems(assessment, result.getApplicationImpacts()));
        items.addAll(mapItems(assessment, result.getMicroserviceImpacts()));
        items.addAll(mapItems(assessment, result.getApiImpacts()));
        items.addAll(mapItems(assessment, result.getDatabaseImpacts()));
        items.addAll(mapItems(assessment, result.getBusinessTeamImpacts()));
        items.addAll(mapItems(assessment, result.getCustomerImpacts()));
        items.addAll(mapItems(assessment, result.getTransactionImpacts()));
        items.addAll(mapItems(assessment, result.getOperationalImpacts()));
        items.addAll(mapItems(assessment, result.getEngineeringImpacts()));
        items.addAll(mapItems(assessment, result.getComplianceRiskImpacts()));
        assessment.getImpactItems().addAll(items);
    }

    private List<EnterpriseImpactItem> mapItems(
            EnterpriseImpactAssessment assessment,
            List<ImpactItemSchema> schemas) {
        return schemas.stream()
                .map(schema -> EnterpriseImpactItem.builder()
                        .assessment(assessment)
                        .category(schema.getCategory())
                        .componentName(schema.getComponentName())
                        .componentType(schema.getComponentType())
                        .reason(schema.getReason())
                        .severity(schema.getSeverity())
                        .confidence(schema.getConfidence())
                        .evidence(schema.getEvidence())
                        .build())
                .toList();
    }

    private EnterpriseImpactAssessmentSchema toSchema(EnterpriseImpactAssessment assessment) {
        List<EnterpriseImpactItem> items = assessment.getImpactItems();
        EnterpriseImpactAssessmentSchema.EnterpriseImpactAssessmentSchemaBuilder builder =
                EnterpriseImpactAssessmentSchema.builder()
                        .id(assessment.getId())
                        .regulationId(assessment.getRegulation().getId())
                        .regulationAnalysisId(assessment.getRegulationAnalysis().getId())
                        .status(assessment.getStatus())
                        .analyzedAt(assessment.getAnalyzedAt())
                        .applicationImpacts(filterItems(items, ImpactCategory.APPLICATION))
                        .microserviceImpacts(filterItems(items, ImpactCategory.MICROSERVICE))
                        .apiImpacts(filterItems(items, ImpactCategory.API))
                        .databaseImpacts(filterItems(items, ImpactCategory.DATABASE))
                        .businessTeamImpacts(filterItems(items, ImpactCategory.BUSINESS_TEAM))
                        .customerImpacts(filterItems(items, ImpactCategory.CUSTOMER))
                        .transactionImpacts(filterItems(items, ImpactCategory.TRANSACTION))
                        .operationalImpacts(filterItems(items, ImpactCategory.OPERATIONAL))
                        .engineeringImpacts(filterItems(items, ImpactCategory.ENGINEERING))
                        .complianceRiskImpacts(filterItems(items, ImpactCategory.COMPLIANCE_RISK));

        try {
            if (assessment.getComplianceRiskSummary() != null) {
                builder.complianceRisk(objectMapper.readValue(
                        assessment.getComplianceRiskSummary(), ComplianceRiskSchema.class));
            }
            if (assessment.getRiskHeatmap() != null) {
                builder.riskHeatmap(objectMapper.readValue(
                        assessment.getRiskHeatmap(), RiskHeatmapSchema.class));
            }
        } catch (JsonProcessingException ex) {
            throw new BusinessException("Failed to deserialize stored assessment metadata");
        }
        return builder.build();
    }

    private EnterpriseImpactAssessmentSchema toSchema(
            EnterpriseImpactAssessment assessment,
            EnterpriseImpactAssessmentSchema computed) {
        EnterpriseImpactAssessmentSchema schema = toSchema(assessment);
        schema.setComplianceRisk(computed.getComplianceRisk());
        schema.setRiskHeatmap(computed.getRiskHeatmap());
        return schema;
    }

    private List<ImpactItemSchema> filterItems(List<EnterpriseImpactItem> items, ImpactCategory category) {
        return items.stream()
                .filter(item -> item.getCategory() == category)
                .map(this::toItemSchema)
                .toList();
    }

    private ImpactItemSchema toItemSchema(EnterpriseImpactItem item) {
        return ImpactItemSchema.builder()
                .componentName(item.getComponentName())
                .componentType(item.getComponentType())
                .category(item.getCategory())
                .reason(item.getReason())
                .severity(item.getSeverity())
                .confidence(item.getConfidence())
                .evidence(item.getEvidence())
                .build();
    }
}
