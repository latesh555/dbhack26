package com.regintel.ai.regintel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegIntelAnalyzeRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 500)
    private String title;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Jurisdiction is required")
    @Size(max = 100)
    private String jurisdiction;

    @NotBlank(message = "Document type is required")
    @Size(max = 100)
    private String documentType;

    @NotBlank(message = "Raw content is required")
    private String rawContent;

    private LocalDate effectiveDate;
}
