package com.regintel.ai.regintel.dto;

import com.regintel.ai.agentorchestration.entity.WorkflowStatus;
import com.regintel.ai.regintel.entity.WorkflowStep;
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
public class RegIntelStatusResponse {

    private UUID workflowId;
    private UUID regulationId;
    private WorkflowStatus status;
    private WorkflowStep currentStep;
    private String failedStep;
    private int retryCount;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorMessage;

    @Builder.Default
    private List<RegIntelAnalysisResponse.StepStatusDto> steps = new ArrayList<>();
}
