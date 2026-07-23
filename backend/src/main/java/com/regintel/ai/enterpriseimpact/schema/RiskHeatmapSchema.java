package com.regintel.ai.enterpriseimpact.schema;

import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskHeatmapSchema {

    @Builder.Default
    private Map<String, ImpactSeverity> domainSeverity = new LinkedHashMap<>();

    private int totalImpacts;
    private int criticalCount;
    private int highCount;
    private int mediumCount;
    private int lowCount;
    private ImpactSeverity overallRisk;
}
