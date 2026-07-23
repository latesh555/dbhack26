package com.regintel.ai.engineeringplanning.jira;

import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import com.regintel.ai.engineeringplanning.schema.FeatureSchema;
import com.regintel.ai.engineeringplanning.schema.StoryTaskSchema;
import com.regintel.ai.engineeringplanning.schema.UserStorySchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockJiraAdapter implements JiraAdapter {

    private static final String PROJECT_KEY = "REGINT";

    @Override
    public JiraSyncResult syncDeliveryPlan(EngineeringDeliveryPlanSchema plan) {
        log.info("Mock Jira sync for delivery plan {} (regulation {})",
                plan.getId(), plan.getRegulationId());

        int stories = 0;
        int tasks = 0;
        int features = 0;

        if (plan.getEpic() != null) {
            plan.getEpic().setEpicId(PROJECT_KEY + "-" + plan.getEpic().getEpicId());
            for (FeatureSchema feature : plan.getEpic().getFeatures()) {
                features++;
                feature.setFeatureId(PROJECT_KEY + "-" + feature.getFeatureId());
                for (UserStorySchema story : feature.getUserStories()) {
                    stories++;
                    story.setJiraIssueKey(PROJECT_KEY + "-" + story.getStoryId());
                    for (StoryTaskSchema task : story.getTasks()) {
                        tasks++;
                        task.setTaskId(PROJECT_KEY + "-" + task.getTaskId());
                    }
                }
            }
        }

        String syncReference = PROJECT_KEY + "-DELIVERY-" + plan.getRegulationId().toString().substring(0, 8);

        return JiraSyncResult.builder()
                .success(true)
                .syncReference(syncReference)
                .message("Mock Jira sync completed successfully")
                .epicsCreated(plan.getEpic() != null ? 1 : 0)
                .featuresCreated(features)
                .storiesCreated(stories)
                .tasksCreated(tasks)
                .build();
    }
}
