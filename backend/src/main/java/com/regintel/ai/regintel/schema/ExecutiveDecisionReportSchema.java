package com.regintel.ai.regintel.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveDecisionReportSchema {

    private String executiveSummary;
    private String whatChanged;
    private String whyItMatters;
    private String enterpriseImpact;
    private String customerImpact;
    private String businessImpact;
    private String operationalImpact;
    private String engineeringReadiness;

    @Builder.Default
    private List<String> keyRisks = new ArrayList<>();

    @Builder.Default
    private List<String> recommendations = new ArrayList<>();

    @Builder.Default
    private List<String> immediateActions = new ArrayList<>();

    @Builder.Default
    private List<String> leadershipDecisionsRequired = new ArrayList<>();

    private BigDecimal overallRiskScore;
    private String implementationTimeline;
}
