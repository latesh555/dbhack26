package com.regintel.ai.regulationintelligence.dto;

import com.regintel.ai.regulationintelligence.entity.AnalysisStatus;
import com.regintel.ai.regulationintelligence.entity.RiskLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public final class RegulationAnalysisDto {

    private RegulationAnalysisDto() {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String summary;
        private String keyRequirements;
        private String complianceAreas;
        private RiskLevel riskLevel;
        private AnalysisStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest extends Request {
        @NotNull(message = "Regulation ID is required")
        private UUID regulationId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private UUID regulationId;
        private String summary;
        private String keyRequirements;
        private String complianceAreas;
        private RiskLevel riskLevel;
        private AnalysisStatus status;
        private LocalDateTime analyzedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
