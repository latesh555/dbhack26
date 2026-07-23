package com.regintel.ai.regulationintelligence.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceReference {

    private Integer pageNumber;

    @NotBlank
    private String section;

    @NotBlank
    private String supportingText;

    @Min(0)
    @Max(1)
    private Double confidenceScore;
}
