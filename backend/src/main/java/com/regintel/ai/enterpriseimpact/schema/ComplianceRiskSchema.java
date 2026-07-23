package com.regintel.ai.enterpriseimpact.schema;

import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceRiskSchema {

    private String summary;
    private ImpactSeverity overallSeverity;
    private int affectedControls;
    private String primaryRegulatoryDriver;
}
