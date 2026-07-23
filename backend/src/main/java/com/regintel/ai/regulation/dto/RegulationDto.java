package com.regintel.ai.regulation.dto;

import com.regintel.ai.regulation.entity.RegulationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class RegulationDto {

    private RegulationDto() {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Title is required")
        @Size(max = 500, message = "Title must not exceed 500 characters")
        private String title;

        @NotBlank(message = "Source is required")
        private String source;

        @NotBlank(message = "Jurisdiction is required")
        @Size(max = 100)
        private String jurisdiction;

        @NotBlank(message = "Document type is required")
        @Size(max = 100)
        private String documentType;

        private String rawContent;
        private RegulationStatus status;
        private LocalDate effectiveDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String title;
        private String source;
        private String jurisdiction;
        private String documentType;
        private String rawContent;
        private RegulationStatus status;
        private LocalDate effectiveDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadResponse {
        private UUID id;
        private String title;
        private String source;
        private String jurisdiction;
        private String documentType;
        private String rawContent;
        private RegulationStatus status;
        private LocalDate effectiveDate;
        private String detectedMediaType;
        private int extractedTextLength;
        private String extractedTextPreview;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
