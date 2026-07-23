package com.regintel.ai.engineeringplanning.dto;

import com.regintel.ai.engineeringplanning.entity.PlanStatus;
import com.regintel.ai.engineeringplanning.entity.Priority;
import com.regintel.ai.engineeringplanning.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class EngineeringPlanDto {

    private EngineeringPlanDto() {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanRequest {
        @NotBlank(message = "Title is required")
        @Size(max = 500)
        private String title;

        private String description;
        private Priority priority;
        private Integer estimatedEffortDays;
        private PlanStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskRequest {
        @NotBlank(message = "Title is required")
        @Size(max = 500)
        private String title;

        private String description;
        private Priority priority;
        private Integer estimatedHours;
        private TaskStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskResponse {
        private UUID id;
        private UUID engineeringPlanId;
        private String title;
        private String description;
        private Priority priority;
        private Integer estimatedHours;
        private TaskStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanResponse {
        private UUID id;
        private UUID impactAssessmentId;
        private String title;
        private String description;
        private Priority priority;
        private Integer estimatedEffortDays;
        private PlanStatus status;
        private List<TaskResponse> tasks;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
