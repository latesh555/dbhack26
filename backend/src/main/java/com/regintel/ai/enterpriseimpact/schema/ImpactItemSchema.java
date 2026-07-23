package com.regintel.ai.enterpriseimpact.schema;

import com.regintel.ai.enterpriseimpact.entity.ComponentType;
import com.regintel.ai.enterpriseimpact.entity.ImpactCategory;
import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImpactItemSchema {

    private String componentName;
    private ComponentType componentType;
    private ImpactCategory category;
    private String reason;
    private ImpactSeverity severity;
    private BigDecimal confidence;
    private String evidence;
}
