package com.regintel.ai.agentorchestration.dto;

import com.regintel.ai.agentorchestration.entity.AgentTaskStatus;
import com.regintel.ai.agentorchestration.entity.AgentType;
import com.regintel.ai.agentorchestration.entity.WorkflowStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class AgentWorkflowDto {

    private AgentWorkflowDto() {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowRequest {
        @NotBlank(message = "Workflow name is required")
        private String name;

        @NotBlank(message = "Workflow type is required")
        private String workflowType;

        private UUID regulationId;
        private WorkflowStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskRequest {
        @NotNull(message = "Agent type is required")
        private AgentType agentType;

        @NotBlank(message = "Task type is required")
        private String taskType;

        private String inputPayload;
        private AgentTaskStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskResponse {
        private UUID id;
        private UUID workflowId;
        private AgentType agentType;
        private String taskType;
        private AgentTaskStatus status;
        private String inputPayload;
        private String outputPayload;
        private String errorMessage;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowResponse {
        private UUID id;
        private String name;
        private String workflowType;
        private WorkflowStatus status;
        private String currentStep;
        private UUID regulationId;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private List<TaskResponse> tasks;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
