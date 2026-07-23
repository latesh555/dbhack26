package com.regintel.ai.regintel.agent;

import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import com.regintel.ai.llm.exception.LlmException;
import com.regintel.ai.llm.service.LlmService;
import com.regintel.ai.llm.support.LlmJsonParser;
import com.regintel.ai.regintel.schema.ExecutiveDecisionReportSchema;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExecutiveDecisionCopilotAgent {

    private final LlmService llmService;
    private final LlmJsonParser llmJsonParser;

    public ExecutiveDecisionReportSchema generate(
            RegulatoryIntelligence intelligence,
            EnterpriseImpactAssessmentSchema impact,
            EngineeringDeliveryPlanSchema plan) {

        if (!llmService.isEnabled() || !llmService.hasAvailableProvider()) {
            throw new LlmException(
                    "LLM provider required for executive decision report. Set GROQ_API_KEY or GEMINI_API_KEY.");
        }

        String json = llmService.completeJson(
                ExecutiveDecisionPromptBuilder.systemPrompt(),
                ExecutiveDecisionPromptBuilder.userPrompt(
                        llmJsonParser.toJson(intelligence),
                        llmJsonParser.toJson(impact),
                        llmJsonParser.toJson(plan)));

        ExecutiveDecisionReportSchema report = llmJsonParser.parse(json, ExecutiveDecisionReportSchema.class);
        normalizeReport(report);

        log.info("LLM executive decision report generated with risk score {}", report.getOverallRiskScore());
        return report;
    }

    private void normalizeReport(ExecutiveDecisionReportSchema report) {
        if (report.getKeyRisks() == null) {
            report.setKeyRisks(new ArrayList<>());
        }
        if (report.getRecommendations() == null) {
            report.setRecommendations(new ArrayList<>());
        }
        if (report.getImmediateActions() == null) {
            report.setImmediateActions(new ArrayList<>());
        }
        if (report.getLeadershipDecisionsRequired() == null) {
            report.setLeadershipDecisionsRequired(new ArrayList<>());
        }
        if (report.getOverallRiskScore() == null) {
            report.setOverallRiskScore(BigDecimal.valueOf(50.0));
        } else {
            report.setOverallRiskScore(report.getOverallRiskScore().setScale(1, RoundingMode.HALF_UP));
        }
    }
}
