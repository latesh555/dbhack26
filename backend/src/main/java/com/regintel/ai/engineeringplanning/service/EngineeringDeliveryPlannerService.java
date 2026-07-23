package com.regintel.ai.engineeringplanning.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.common.exception.BusinessException;
import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.enterpriseimpact.entity.EnterpriseImpactAssessment;
import com.regintel.ai.enterpriseimpact.repository.EnterpriseImpactAssessmentRepository;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.enterpriseimpact.service.EnterpriseImpactAnalystService;
import com.regintel.ai.engineeringplanning.agent.EngineeringDeliveryPlannerAgent;
import com.regintel.ai.engineeringplanning.entity.*;
import com.regintel.ai.engineeringplanning.jira.JiraAdapter;
import com.regintel.ai.engineeringplanning.jira.JiraSyncResult;
import com.regintel.ai.engineeringplanning.repository.DeliveryPlanRepository;
import com.regintel.ai.engineeringplanning.repository.DeliveryUserStoryRepository;
import com.regintel.ai.engineeringplanning.schema.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EngineeringDeliveryPlannerService {

    private final EngineeringDeliveryPlannerAgent plannerAgent;
    private final EnterpriseImpactAnalystService impactAnalystService;
    private final EnterpriseImpactAssessmentRepository impactAssessmentRepository;
    private final DeliveryPlanRepository deliveryPlanRepository;
    private final DeliveryUserStoryRepository userStoryRepository;
    private final JiraAdapter jiraAdapter;
    private final ObjectMapper objectMapper;

    @Transactional
    public EngineeringDeliveryPlanSchema generatePlan(UUID regulationId) {
        log.info("Generating engineering delivery plan for regulation: {}", regulationId);

        EnterpriseImpactAssessmentSchema impactAssessment = impactAnalystService.getByRegulationId(regulationId);
        EnterpriseImpactAssessment impactEntity = impactAssessmentRepository.findById(impactAssessment.getId())
                .orElseThrow(() -> new ResourceNotFoundException("EnterpriseImpactAssessment", impactAssessment.getId()));

        DeliveryPlan plan = DeliveryPlan.builder()
                .enterpriseImpactAssessment(impactEntity)
                .regulation(impactEntity.getRegulation())
                .status(DeliveryPlanStatus.GENERATING)
                .build();
        plan = deliveryPlanRepository.save(plan);

        try {
            EngineeringDeliveryPlanSchema schema = plannerAgent.generatePlan(impactAssessment);
            persistPlan(plan, schema);

            JiraSyncResult jiraResult = jiraAdapter.syncDeliveryPlan(schema);
            applyJiraSync(plan, schema, jiraResult);

            plan.setStatus(DeliveryPlanStatus.SYNCED_TO_JIRA);
            plan.setGeneratedAt(LocalDateTime.now());
            plan.setJiraSyncReference(jiraResult.getSyncReference());
            deliveryPlanRepository.save(plan);

            schema.setId(plan.getId());
            schema.setStatus(plan.getStatus());
            schema.setGeneratedAt(plan.getGeneratedAt());
            schema.setJiraSyncReference(plan.getJiraSyncReference());
            return schema;
        } catch (Exception ex) {
            log.error("Delivery plan generation failed for regulation {}", regulationId, ex);
            plan.setStatus(DeliveryPlanStatus.FAILED);
            plan.setErrorMessage(ex.getMessage());
            deliveryPlanRepository.save(plan);
            throw new BusinessException("Delivery plan generation failed: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public EngineeringDeliveryPlanSchema getPlanByRegulationId(UUID regulationId) {
        DeliveryPlan plan = deliveryPlanRepository
                .findFirstByRegulation_IdAndStatusOrderByGeneratedAtDesc(regulationId, DeliveryPlanStatus.SYNCED_TO_JIRA)
                .or(() -> deliveryPlanRepository.findFirstByRegulation_IdAndStatusOrderByGeneratedAtDesc(
                        regulationId, DeliveryPlanStatus.GENERATED))
                .orElseThrow(() -> new ResourceNotFoundException("Delivery plan for regulation", regulationId));
        return toSchema(plan);
    }

    @Transactional(readOnly = true)
    public List<EpicSchema> getEpics(UUID regulationId) {
        DeliveryPlan plan = getPlanEntity(regulationId);
        return plan.getEpics().stream().map(this::toEpicSchema).toList();
    }

    @Transactional(readOnly = true)
    public List<UserStorySchema> getUserStories(UUID regulationId) {
        DeliveryPlan plan = getPlanEntity(regulationId);
        List<UserStorySchema> stories = new ArrayList<>();
        for (DeliveryEpic epic : plan.getEpics()) {
            for (DeliveryFeature feature : epic.getFeatures()) {
                for (DeliveryUserStory story : feature.getUserStories()) {
                    stories.add(toUserStorySchema(story));
                }
            }
        }
        return stories;
    }

    @Transactional
    public UserStorySchema updateStoryStatus(UUID regulationId, UUID storyId, UserStoryStatus status) {
        DeliveryUserStory story = userStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryUserStory", storyId));
        UUID storyRegulationId = story.getFeature().getEpic().getDeliveryPlan().getRegulation().getId();
        if (!storyRegulationId.equals(regulationId)) {
            throw new BusinessException("User story does not belong to regulation: " + regulationId);
        }
        story.setStatus(status);
        return toUserStorySchema(userStoryRepository.save(story));
    }

    private DeliveryPlan getPlanEntity(UUID regulationId) {
        return deliveryPlanRepository
                .findFirstByRegulation_IdOrderByGeneratedAtDesc(regulationId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery plan for regulation", regulationId));
    }

    private void persistPlan(DeliveryPlan plan, EngineeringDeliveryPlanSchema schema) throws JsonProcessingException {
        plan.setAffectedApis(objectMapper.writeValueAsString(schema.getAffectedApis()));
        plan.setAffectedMicroservices(objectMapper.writeValueAsString(schema.getAffectedMicroservices()));

        if (schema.getStrategies() != null) {
            plan.setTestingStrategy(schema.getStrategies().getTestingStrategy());
            plan.setDeploymentStrategy(schema.getStrategies().getDeploymentStrategy());
            plan.setRollbackStrategy(schema.getStrategies().getRollbackStrategy());
            plan.setProductionValidationChecklist(objectMapper.writeValueAsString(
                    schema.getStrategies().getProductionValidationChecklist()));
        }

        if (schema.getEpic() != null) {
            DeliveryEpic epic = mapEpic(plan, schema.getEpic());
            plan.getEpics().add(epic);
        }

        plan.setStatus(DeliveryPlanStatus.GENERATED);
    }

    private DeliveryEpic mapEpic(DeliveryPlan plan, EpicSchema epicSchema) throws JsonProcessingException {
        DeliveryEpic epic = DeliveryEpic.builder()
                .deliveryPlan(plan)
                .epicKey(epicSchema.getEpicId())
                .title(epicSchema.getTitle())
                .description(epicSchema.getDescription())
                .priority(epicSchema.getPriority())
                .ownerTeam(epicSchema.getOwnerTeam())
                .totalStoryPoints(epicSchema.getTotalStoryPoints())
                .build();

        for (FeatureSchema featureSchema : epicSchema.getFeatures()) {
            epic.getFeatures().add(mapFeature(epic, featureSchema));
        }
        return epic;
    }

    private DeliveryFeature mapFeature(DeliveryEpic epic, FeatureSchema featureSchema) throws JsonProcessingException {
        DeliveryFeature feature = DeliveryFeature.builder()
                .epic(epic)
                .featureKey(featureSchema.getFeatureId())
                .title(featureSchema.getTitle())
                .description(featureSchema.getDescription())
                .priority(featureSchema.getPriority())
                .ownerTeam(featureSchema.getOwnerTeam())
                .build();

        for (UserStorySchema storySchema : featureSchema.getUserStories()) {
            feature.getUserStories().add(mapUserStory(feature, storySchema));
        }
        return feature;
    }

    private DeliveryUserStory mapUserStory(DeliveryFeature feature, UserStorySchema storySchema)
            throws JsonProcessingException {
        DeliveryUserStory story = DeliveryUserStory.builder()
                .feature(feature)
                .storyKey(storySchema.getStoryId())
                .title(storySchema.getTitle())
                .description(storySchema.getDescription())
                .priority(storySchema.getPriority())
                .storyPoints(storySchema.getStoryPoints())
                .ownerTeam(storySchema.getOwnerTeam())
                .dependencies(objectMapper.writeValueAsString(storySchema.getDependencies()))
                .affectedComponents(objectMapper.writeValueAsString(storySchema.getAffectedComponents()))
                .acceptanceCriteria(objectMapper.writeValueAsString(storySchema.getAcceptanceCriteria()))
                .testingChecklist(objectMapper.writeValueAsString(storySchema.getTestingChecklist()))
                .status(storySchema.getStatus() != null ? storySchema.getStatus() : UserStoryStatus.TODO)
                .jiraIssueKey(storySchema.getJiraIssueKey())
                .build();

        for (StoryTaskSchema taskSchema : storySchema.getTasks()) {
            story.getTasks().add(DeliveryStoryTask.builder()
                    .userStory(story)
                    .taskKey(taskSchema.getTaskId())
                    .title(taskSchema.getTitle())
                    .description(taskSchema.getDescription())
                    .priority(taskSchema.getPriority())
                    .ownerTeam(taskSchema.getOwnerTeam())
                    .status(taskSchema.getStatus())
                    .build());
        }
        return story;
    }

    private void applyJiraSync(DeliveryPlan plan, EngineeringDeliveryPlanSchema schema, JiraSyncResult jiraResult) {
        if (!jiraResult.isSuccess() || schema.getEpic() == null) {
            return;
        }
        DeliveryEpic epicEntity = plan.getEpics().isEmpty() ? null : plan.getEpics().getFirst();
        if (epicEntity == null) {
            return;
        }
        epicEntity.setEpicKey(schema.getEpic().getEpicId());

        int featureIdx = 0;
        for (FeatureSchema featureSchema : schema.getEpic().getFeatures()) {
            if (featureIdx >= epicEntity.getFeatures().size()) {
                break;
            }
            DeliveryFeature featureEntity = epicEntity.getFeatures().get(featureIdx++);
            featureEntity.setFeatureKey(featureSchema.getFeatureId());

            int storyIdx = 0;
            for (UserStorySchema storySchema : featureSchema.getUserStories()) {
                if (storyIdx >= featureEntity.getUserStories().size()) {
                    break;
                }
                DeliveryUserStory storyEntity = featureEntity.getUserStories().get(storyIdx++);
                storyEntity.setJiraIssueKey(storySchema.getJiraIssueKey());

                int taskIdx = 0;
                for (StoryTaskSchema taskSchema : storySchema.getTasks()) {
                    if (taskIdx >= storyEntity.getTasks().size()) {
                        break;
                    }
                    storyEntity.getTasks().get(taskIdx++).setTaskKey(taskSchema.getTaskId());
                }
            }
        }
    }

    private EngineeringDeliveryPlanSchema toSchema(DeliveryPlan plan) {
        EngineeringDeliveryPlanSchema.EngineeringDeliveryPlanSchemaBuilder builder =
                EngineeringDeliveryPlanSchema.builder()
                        .id(plan.getId())
                        .enterpriseImpactAssessmentId(plan.getEnterpriseImpactAssessment().getId())
                        .regulationId(plan.getRegulation().getId())
                        .status(plan.getStatus())
                        .generatedAt(plan.getGeneratedAt())
                        .jiraSyncReference(plan.getJiraSyncReference());

        try {
            if (plan.getAffectedApis() != null) {
                builder.affectedApis(objectMapper.readValue(plan.getAffectedApis(), new TypeReference<>() {}));
            }
            if (plan.getAffectedMicroservices() != null) {
                builder.affectedMicroservices(objectMapper.readValue(
                        plan.getAffectedMicroservices(), new TypeReference<>() {}));
            }
            builder.strategies(DeliveryStrategySchema.builder()
                    .testingStrategy(plan.getTestingStrategy())
                    .deploymentStrategy(plan.getDeploymentStrategy())
                    .rollbackStrategy(plan.getRollbackStrategy())
                    .productionValidationChecklist(plan.getProductionValidationChecklist() != null
                            ? objectMapper.readValue(plan.getProductionValidationChecklist(), new TypeReference<>() {})
                            : List.of())
                    .build());
        } catch (JsonProcessingException ex) {
            throw new BusinessException("Failed to deserialize delivery plan metadata");
        }

        if (!plan.getEpics().isEmpty()) {
            builder.epic(toEpicSchema(plan.getEpics().getFirst()));
        }
        return builder.build();
    }

    private EpicSchema toEpicSchema(DeliveryEpic epic) {
        return EpicSchema.builder()
                .epicId(epic.getEpicKey())
                .title(epic.getTitle())
                .description(epic.getDescription())
                .priority(epic.getPriority())
                .ownerTeam(epic.getOwnerTeam())
                .totalStoryPoints(epic.getTotalStoryPoints())
                .features(epic.getFeatures().stream().map(this::toFeatureSchema).toList())
                .build();
    }

    private FeatureSchema toFeatureSchema(DeliveryFeature feature) {
        return FeatureSchema.builder()
                .featureId(feature.getFeatureKey())
                .title(feature.getTitle())
                .description(feature.getDescription())
                .priority(feature.getPriority())
                .ownerTeam(feature.getOwnerTeam())
                .userStories(feature.getUserStories().stream().map(this::toUserStorySchema).toList())
                .build();
    }

    private UserStorySchema toUserStorySchema(DeliveryUserStory story) {
        try {
            return UserStorySchema.builder()
                    .id(story.getId())
                    .storyId(story.getStoryKey())
                    .title(story.getTitle())
                    .description(story.getDescription())
                    .priority(story.getPriority())
                    .storyPoints(story.getStoryPoints())
                    .ownerTeam(story.getOwnerTeam())
                    .dependencies(readList(story.getDependencies()))
                    .affectedComponents(readList(story.getAffectedComponents()))
                    .acceptanceCriteria(readList(story.getAcceptanceCriteria()))
                    .testingChecklist(readList(story.getTestingChecklist()))
                    .tasks(story.getTasks().stream().map(this::toTaskSchema).toList())
                    .status(story.getStatus())
                    .jiraIssueKey(story.getJiraIssueKey())
                    .build();
        } catch (JsonProcessingException ex) {
            throw new BusinessException("Failed to deserialize user story: " + story.getStoryKey());
        }
    }

    private StoryTaskSchema toTaskSchema(DeliveryStoryTask task) {
        return StoryTaskSchema.builder()
                .taskId(task.getTaskKey())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .ownerTeam(task.getOwnerTeam())
                .status(task.getStatus())
                .build();
    }

    private List<String> readList(String json) throws JsonProcessingException {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        return objectMapper.readValue(json, new TypeReference<>() {});
    }
}
