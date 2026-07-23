package com.regintel.ai.engineeringplanning.jira;

import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JiraSyncResult {

    boolean success;
    String syncReference;
    String message;
    int epicsCreated;
    int featuresCreated;
    int storiesCreated;
    int tasksCreated;
}
