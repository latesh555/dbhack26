package com.regintel.ai.regintel.dto;

import com.regintel.ai.agentorchestration.entity.WorkflowStatus;
import com.regintel.ai.regintel.entity.WorkflowStep;
import com.regintel.ai.regintel.schema.ExecutiveDecisionReportSchema;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegIntelAnalysisResponse {

    private UUID workflowId;
    private UUID regulationId;
    private WorkflowStatus status;
    private WorkflowStep currentStep;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private RegulatoryIntelligence regulationIntelligence;
    private EnterpriseImpactAssessmentSchema enterpriseImpact;
    private EngineeringDeliveryPlanSchema engineeringPlan;
    private ExecutiveDecisionReportSchema executiveReport;

    @Builder.Default
    private List<StepStatusDto> steps = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepStatusDto {
        private WorkflowStep step;
        private String status;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private String errorMessage;
        private int retryCount;
    }
}
