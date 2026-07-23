package com.regintel.ai.engineeringplanning.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStrategySchema {

    private String testingStrategy;
    private String deploymentStrategy;
    private String rollbackStrategy;

    @Builder.Default
    private List<String> productionValidationChecklist = new ArrayList<>();
}
