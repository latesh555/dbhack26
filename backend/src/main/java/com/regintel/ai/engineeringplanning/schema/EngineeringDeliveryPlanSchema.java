package com.regintel.ai.engineeringplanning.schema;

import com.regintel.ai.engineeringplanning.entity.DeliveryPlanStatus;
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
public class EngineeringDeliveryPlanSchema {

    private UUID id;
    private UUID enterpriseImpactAssessmentId;
    private UUID regulationId;
    private DeliveryPlanStatus status;
    private LocalDateTime generatedAt;

    private EpicSchema epic;

    @Builder.Default
    private List<String> affectedApis = new ArrayList<>();

    @Builder.Default
    private List<String> affectedMicroservices = new ArrayList<>();

    private DeliveryStrategySchema strategies;
    private String jiraSyncReference;
}
