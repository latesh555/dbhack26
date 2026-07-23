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
public class RequirementChange {

    @NotBlank
    private String changeId;

    @NotBlank
    private String summary;

    @NotBlank
    private String changeType;

    @Valid
    private SourceReference sourceReference;
}
