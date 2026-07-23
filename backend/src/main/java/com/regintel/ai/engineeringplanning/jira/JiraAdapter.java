package com.regintel.ai.engineeringplanning.jira;

import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;

public interface JiraAdapter {

    JiraSyncResult syncDeliveryPlan(EngineeringDeliveryPlanSchema plan);
}
