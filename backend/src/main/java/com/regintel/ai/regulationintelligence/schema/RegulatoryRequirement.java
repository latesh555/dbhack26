package com.regintel.ai.regulationintelligence.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
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
public class RegulatoryRequirement {

    @NotBlank
    private String requirementId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Valid
    private SourceReference sourceReference;
}
