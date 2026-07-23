package com.regintel.ai.executivereporting.dto;

import com.regintel.ai.executivereporting.entity.ReportStatus;
import com.regintel.ai.executivereporting.entity.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ExecutiveReportDto {

    private ExecutiveReportDto() {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Title is required")
        @Size(max = 500)
        private String title;

        @NotNull(message = "Report type is required")
        private ReportType reportType;

        private String content;
        private UUID regulationId;
        private ReportStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String title;
        private ReportType reportType;
        private String content;
        private UUID regulationId;
        private ReportStatus status;
        private LocalDateTime generatedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
