package com.regintel.ai.regulationintelligence.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegulatoryIntelligence {

    @NotBlank
    private String documentType;

    @NotBlank
    private String regulatoryAuthority;

    private LocalDate publicationDate;

    private LocalDate effectiveDate;

    @NotBlank
    private String severity;

    @NotBlank
    private String executiveSummary;

    @Valid
    @Builder.Default
    private List<RequirementChange> keyChanges = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<RegulatoryRequirement> newRequirements = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<RegulatoryRequirement> removedRequirements = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<RegulatoryRequirement> modifiedRequirements = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<RegulatoryDeadline> deadlines = new ArrayList<>();

    @Builder.Default
    private List<String> jurisdictions = new ArrayList<>();

    @Builder.Default
    private List<String> industries = new ArrayList<>();

    private String sanctionsInformation;

    @Builder.Default
    private List<String> businessDomains = new ArrayList<>();
}
