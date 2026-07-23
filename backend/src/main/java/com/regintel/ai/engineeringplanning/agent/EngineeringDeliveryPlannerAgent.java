package com.regintel.ai.engineeringplanning.agent;

import com.regintel.ai.engineeringplanning.entity.Priority;
import com.regintel.ai.engineeringplanning.entity.UserStoryStatus;
import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import com.regintel.ai.engineeringplanning.schema.EpicSchema;
import com.regintel.ai.engineeringplanning.schema.FeatureSchema;
import com.regintel.ai.engineeringplanning.schema.UserStorySchema;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.llm.exception.LlmException;
import com.regintel.ai.llm.service.LlmService;
import com.regintel.ai.llm.support.LlmJsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EngineeringDeliveryPlannerAgent {

    private final LlmService llmService;
    private final LlmJsonParser llmJsonParser;

    public EngineeringDeliveryPlanSchema generatePlan(EnterpriseImpactAssessmentSchema assessment) {
        if (!llmService.isEnabled() || !llmService.hasAvailableProvider()) {
            throw new LlmException(
                    "LLM provider required for engineering delivery planning. Set GROQ_API_KEY or GEMINI_API_KEY.");
        }

        String impactJson = llmJsonParser.toJson(assessment);
        String json = llmService.completeJson(
                EngineeringDeliveryPromptBuilder.systemPrompt(),
                EngineeringDeliveryPromptBuilder.userPrompt(impactJson));

        EngineeringDeliveryPlanSchema plan = llmJsonParser.parse(json, EngineeringDeliveryPlanSchema.class);
        enrichPlan(plan, assessment);
        normalizePlan(plan);

        log.info("LLM engineering delivery plan generated with {} user stories",
                countUserStories(plan));
        return plan;
    }

    private void enrichPlan(EngineeringDeliveryPlanSchema plan, EnterpriseImpactAssessmentSchema assessment) {
        plan.setEnterpriseImpactAssessmentId(assessment.getId());
        plan.setRegulationId(assessment.getRegulationId());
        if (plan.getAffectedApis() == null) {
            plan.setAffectedApis(new ArrayList<>());
        }
        if (plan.getAffectedMicroservices() == null) {
            plan.setAffectedMicroservices(new ArrayList<>());
        }
    }

    private void normalizePlan(EngineeringDeliveryPlanSchema plan) {
        if (plan.getEpic() == null) {
            plan.setEpic(EpicSchema.builder()
                    .epicId("EPIC-001")
                    .title("Regulatory Delivery Plan")
                    .features(new ArrayList<>())
                    .build());
        }

        EpicSchema epic = plan.getEpic();
        if (epic.getFeatures() == null) {
            epic.setFeatures(new ArrayList<>());
        }

        int totalPoints = 0;
        for (FeatureSchema feature : epic.getFeatures()) {
            if (feature.getUserStories() == null) {
                feature.setUserStories(new ArrayList<>());
            }
            for (UserStorySchema story : feature.getUserStories()) {
                if (story.getStatus() == null) {
                    story.setStatus(UserStoryStatus.TODO);
                }
                if (story.getPriority() == null) {
                    story.setPriority(Priority.MEDIUM);
                }
                if (story.getDependencies() == null) {
                    story.setDependencies(new ArrayList<>());
                }
                if (story.getAffectedComponents() == null) {
                    story.setAffectedComponents(new ArrayList<>());
                }
                if (story.getAcceptanceCriteria() == null) {
                    story.setAcceptanceCriteria(new ArrayList<>());
                }
                if (story.getTestingChecklist() == null) {
                    story.setTestingChecklist(new ArrayList<>());
                }
                if (story.getTasks() == null) {
                    story.setTasks(new ArrayList<>());
                }
                if (story.getStoryPoints() != null) {
                    totalPoints += story.getStoryPoints();
                }
            }
        }

        if (epic.getTotalStoryPoints() == null || epic.getTotalStoryPoints() == 0) {
            epic.setTotalStoryPoints(totalPoints);
        }
        if (epic.getPriority() == null) {
            epic.setPriority(Priority.HIGH);
        }
    }

    private int countUserStories(EngineeringDeliveryPlanSchema plan) {
        if (plan.getEpic() == null || plan.getEpic().getFeatures() == null) {
            return 0;
        }
        return plan.getEpic().getFeatures().stream()
                .mapToInt(f -> f.getUserStories() != null ? f.getUserStories().size() : 0)
                .sum();
    }
}
