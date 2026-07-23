package com.regintel.ai.enterpriseimpact.schema;

import com.regintel.ai.enterpriseimpact.entity.EnterpriseAssessmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseImpactAssessmentSchema {

    private UUID id;
    private UUID regulationId;
    private UUID regulationAnalysisId;
    private EnterpriseAssessmentStatus status;
    private LocalDateTime analyzedAt;

    @Builder.Default
    private List<ImpactItemSchema> applicationImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> microserviceImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> apiImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> databaseImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> businessTeamImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> customerImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> transactionImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> operationalImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> engineeringImpacts = new ArrayList<>();

    @Builder.Default
    private List<ImpactItemSchema> complianceRiskImpacts = new ArrayList<>();

    private ComplianceRiskSchema complianceRisk;
    private RiskHeatmapSchema riskHeatmap;
}
