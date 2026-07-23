package com.regintel.ai.enterpriseimpact.agent;

import com.regintel.ai.enterpriseimpact.knowledge.MockEnterpriseKnowledgeBase;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.llm.exception.LlmException;
import com.regintel.ai.llm.service.LlmService;
import com.regintel.ai.llm.support.LlmJsonParser;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnterpriseImpactAnalystAgent {

    private final LlmService llmService;
    private final LlmJsonParser llmJsonParser;
    private final MockEnterpriseKnowledgeBase knowledgeBase;

    public EnterpriseImpactAssessmentSchema analyze(RegulatoryIntelligence intelligence) {
        if (!llmService.isEnabled() || !llmService.hasAvailableProvider()) {
            throw new LlmException(
                    "LLM provider required for enterprise impact analysis. Set GROQ_API_KEY or GEMINI_API_KEY.");
        }

        String json = llmService.completeJson(
                EnterpriseImpactPromptBuilder.systemPrompt(),
                EnterpriseImpactPromptBuilder.userPrompt(intelligence, knowledgeBase.getAllComponents()));

        EnterpriseImpactAssessmentSchema assessment = llmJsonParser.parse(json, EnterpriseImpactAssessmentSchema.class);
        normalizeLists(assessment);

        log.info("LLM enterprise impact analysis completed with {} total impacts",
                countImpacts(assessment));
        return assessment;
    }

    private void normalizeLists(EnterpriseImpactAssessmentSchema assessment) {
        if (assessment.getApplicationImpacts() == null) {
            assessment.setApplicationImpacts(new ArrayList<>());
        }
        if (assessment.getMicroserviceImpacts() == null) {
            assessment.setMicroserviceImpacts(new ArrayList<>());
        }
        if (assessment.getApiImpacts() == null) {
            assessment.setApiImpacts(new ArrayList<>());
        }
        if (assessment.getDatabaseImpacts() == null) {
            assessment.setDatabaseImpacts(new ArrayList<>());
        }
        if (assessment.getBusinessTeamImpacts() == null) {
            assessment.setBusinessTeamImpacts(new ArrayList<>());
        }
        if (assessment.getCustomerImpacts() == null) {
            assessment.setCustomerImpacts(new ArrayList<>());
        }
        if (assessment.getTransactionImpacts() == null) {
            assessment.setTransactionImpacts(new ArrayList<>());
        }
        if (assessment.getOperationalImpacts() == null) {
            assessment.setOperationalImpacts(new ArrayList<>());
        }
        if (assessment.getEngineeringImpacts() == null) {
            assessment.setEngineeringImpacts(new ArrayList<>());
        }
        if (assessment.getComplianceRiskImpacts() == null) {
            assessment.setComplianceRiskImpacts(new ArrayList<>());
        }
    }

    private int countImpacts(EnterpriseImpactAssessmentSchema assessment) {
        return List.of(
                assessment.getApplicationImpacts(),
                assessment.getMicroserviceImpacts(),
                assessment.getApiImpacts(),
                assessment.getDatabaseImpacts(),
                assessment.getBusinessTeamImpacts(),
                assessment.getCustomerImpacts(),
                assessment.getTransactionImpacts(),
                assessment.getOperationalImpacts(),
                assessment.getEngineeringImpacts(),
                assessment.getComplianceRiskImpacts()
        ).stream().mapToInt(List::size).sum();
    }
}
