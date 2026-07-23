package com.regintel.ai.enterpriseimpact.knowledge;

import com.regintel.ai.enterpriseimpact.entity.ComponentType;
import com.regintel.ai.enterpriseimpact.entity.ImpactCategory;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class KnowledgeBaseComponent {

    String name;
    ComponentType componentType;
    ImpactCategory category;
    List<String> tags;
    List<String> jurisdictions;
    List<String> industries;
    List<String> businessDomains;
    String description;
}
