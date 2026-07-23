package com.regintel.ai.enterpriseimpact.dto;

import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import com.regintel.ai.enterpriseimpact.entity.ImpactStatus;
import com.regintel.ai.enterpriseimpact.entity.ImpactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ImpactAssessmentDto {

    private ImpactAssessmentDto() {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Business unit is required")
        private String businessUnit;

        @NotNull(message = "Impact type is required")
        private ImpactType impactType;

        private ImpactSeverity severity;
        private String description;
        private BigDecimal estimatedCost;
        private ImpactStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private UUID regulationAnalysisId;
        private String businessUnit;
        private ImpactType impactType;
        private ImpactSeverity severity;
        private String description;
        private BigDecimal estimatedCost;
        private ImpactStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
