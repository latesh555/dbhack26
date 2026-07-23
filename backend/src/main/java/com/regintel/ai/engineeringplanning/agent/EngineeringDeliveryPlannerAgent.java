package com.regintel.ai.engineeringplanning.agent;

import com.regintel.ai.enterpriseimpact.entity.ComponentType;
import com.regintel.ai.enterpriseimpact.entity.ImpactCategory;
import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.enterpriseimpact.schema.ImpactItemSchema;
import com.regintel.ai.engineeringplanning.entity.Priority;
import com.regintel.ai.engineeringplanning.entity.UserStoryStatus;
import com.regintel.ai.engineeringplanning.schema.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
public class EngineeringDeliveryPlannerAgent {

    private static final Map<ImpactCategory, String> FEATURE_TITLES = Map.ofEntries(
            Map.entry(ImpactCategory.APPLICATION, "Application Remediation"),
            Map.entry(ImpactCategory.MICROSERVICE, "Microservice Updates"),
            Map.entry(ImpactCategory.API, "API Contract Changes"),
            Map.entry(ImpactCategory.DATABASE, "Database & Data Model Changes"),
            Map.entry(ImpactCategory.BUSINESS_TEAM, "Business Team Enablement"),
            Map.entry(ImpactCategory.CUSTOMER, "Customer Communication & Controls"),
            Map.entry(ImpactCategory.TRANSACTION, "Transaction Flow Updates"),
            Map.entry(ImpactCategory.OPERATIONAL, "Operational Process Changes"),
            Map.entry(ImpactCategory.ENGINEERING, "Engineering Enablement"),
            Map.entry(ImpactCategory.COMPLIANCE_RISK, "Compliance Control Updates")
    );

    public EngineeringDeliveryPlanSchema generatePlan(EnterpriseImpactAssessmentSchema assessment) {
        List<ImpactItemSchema> allImpacts = collectAllImpacts(assessment);

        List<String> affectedApis = extractComponentNames(allImpacts, ComponentType.API);
        List<String> affectedMicroservices = extractComponentNames(allImpacts, ComponentType.MICROSERVICE);

        Map<ImpactCategory, List<ImpactItemSchema>> grouped = groupByCategory(allImpacts);
        List<FeatureSchema> features = new ArrayList<>();
        int storyCounter = 1;
        int featureCounter = 1;
        List<String> databaseStoryIds = new ArrayList<>();

        for (Map.Entry<ImpactCategory, List<ImpactItemSchema>> entry : grouped.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            String featureKey = "FEAT-" + String.format("%03d", featureCounter++);
            List<UserStorySchema> stories = new ArrayList<>();

            for (ImpactItemSchema impact : entry.getValue()) {
                String storyKey = "US-" + String.format("%03d", storyCounter++);
                UserStorySchema story = buildUserStory(storyKey, impact, entry.getKey());
                if (entry.getKey() == ImpactCategory.DATABASE) {
                    databaseStoryIds.add(storyKey);
                } else if (!databaseStoryIds.isEmpty()
                        && (entry.getKey() == ImpactCategory.API || entry.getKey() == ImpactCategory.MICROSERVICE)) {
                    story.getDependencies().addAll(databaseStoryIds);
                }
                stories.add(story);
            }

            Priority featurePriority = highestPriority(entry.getValue());
            features.add(FeatureSchema.builder()
                    .featureId(featureKey)
                    .title(FEATURE_TITLES.getOrDefault(entry.getKey(), entry.getKey().name() + " Changes"))
                    .description("Delivery feature covering " + entry.getValue().size()
                            + " impacted component(s) in category " + entry.getKey())
                    .priority(featurePriority)
                    .ownerTeam(resolveOwnerTeam(entry.getValue().getFirst()))
                    .userStories(stories)
                    .build());
        }

        int totalPoints = features.stream()
                .flatMap(f -> f.getUserStories().stream())
                .mapToInt(UserStorySchema::getStoryPoints)
                .sum();

        Priority epicPriority = highestPriority(allImpacts);

        EpicSchema epic = EpicSchema.builder()
                .epicId("EPIC-001")
                .title("Regulatory Delivery – Impact Remediation")
                .description("Executable engineering delivery plan derived from enterprise impact assessment "
                        + assessment.getId())
                .priority(epicPriority)
                .ownerTeam("Regulatory Engineering Program")
                .totalStoryPoints(totalPoints)
                .features(features)
                .build();

        return EngineeringDeliveryPlanSchema.builder()
                .enterpriseImpactAssessmentId(assessment.getId())
                .regulationId(assessment.getRegulationId())
                .epic(epic)
                .affectedApis(affectedApis)
                .affectedMicroservices(affectedMicroservices)
                .strategies(buildStrategies(assessment, allImpacts))
                .build();
    }

    private UserStorySchema buildUserStory(String storyKey, ImpactItemSchema impact, ImpactCategory category) {
        Priority priority = mapSeverityToPriority(impact.getSeverity());
        int storyPoints = mapSeverityToStoryPoints(impact.getSeverity());
        String ownerTeam = resolveOwnerTeam(impact);

        return UserStorySchema.builder()
                .storyId(storyKey)
                .title("Remediate impact on " + impact.getComponentName())
                .description("As a " + ownerTeam + " member, implement changes for "
                        + impact.getComponentName() + " to address regulatory impact: "
                        + impact.getReason())
                .priority(priority)
                .storyPoints(storyPoints)
                .ownerTeam(ownerTeam)
                .affectedComponents(List.of(impact.getComponentName()))
                .acceptanceCriteria(buildAcceptanceCriteria(impact))
                .testingChecklist(buildTestingChecklist(impact, category))
                .tasks(buildTasks(storyKey, impact, ownerTeam, priority))
                .status(UserStoryStatus.TODO)
                .build();
    }

    private List<StoryTaskSchema> buildTasks(
            String storyKey, ImpactItemSchema impact, String ownerTeam, Priority priority) {
        return List.of(
                task(storyKey + "-T1", "Analyze impact on " + impact.getComponentName(),
                        "Review impact evidence and confirm change scope", priority, ownerTeam),
                task(storyKey + "-T2", "Implement remediation for " + impact.getComponentName(),
                        "Apply required code, configuration, or process changes", priority, ownerTeam),
                task(storyKey + "-T3", "Test " + impact.getComponentName() + " changes",
                        "Execute unit, integration, and regression tests", priority, ownerTeam),
                task(storyKey + "-T4", "Deploy and validate " + impact.getComponentName(),
                        "Deploy to staging/production and complete validation checklist", priority, ownerTeam)
        );
    }

    private StoryTaskSchema task(String taskId, String title, String description, Priority priority, String ownerTeam) {
        return StoryTaskSchema.builder()
                .taskId(taskId)
                .title(title)
                .description(description)
                .priority(priority)
                .ownerTeam(ownerTeam)
                .status("TODO")
                .build();
    }

    private List<String> buildAcceptanceCriteria(ImpactItemSchema impact) {
        return List.of(
                impact.getComponentName() + " satisfies the regulatory requirement described in the impact assessment",
                "Evidence documented: " + truncate(impact.getEvidence(), 200),
                "Change validated with confidence threshold >= " + impact.getConfidence(),
                "No regression in dependent transaction flows",
                "Compliance sign-off recorded for " + impact.getComponentName()
        );
    }

    private List<String> buildTestingChecklist(ImpactItemSchema impact, ImpactCategory category) {
        List<String> checklist = new ArrayList<>(List.of(
                "Verify " + impact.getComponentName() + " behavior against regulatory evidence",
                "Run regression suite for impacted domain",
                "Validate audit logging and traceability",
                "Confirm rollback path is documented and tested"
        ));
        if (category == ImpactCategory.API || category == ImpactCategory.MICROSERVICE) {
            checklist.add("Execute contract tests for affected API consumers");
            checklist.add("Run performance smoke tests under production-like load");
        }
        if (category == ImpactCategory.DATABASE) {
            checklist.add("Validate migration scripts in staging with production-like data volume");
            checklist.add("Verify data retention and backup policies");
        }
        if (category == ImpactCategory.COMPLIANCE_RISK) {
            checklist.add("Compliance control attestation signed off");
        }
        return checklist;
    }

    private DeliveryStrategySchema buildStrategies(
            EnterpriseImpactAssessmentSchema assessment,
            List<ImpactItemSchema> impacts) {

        String riskLevel = assessment.getRiskHeatmap() != null
                ? assessment.getRiskHeatmap().getOverallRisk().name()
                : "MEDIUM";

        return DeliveryStrategySchema.builder()
                .testingStrategy("Execute risk-based testing aligned to " + riskLevel
                        + " overall risk. Run automated regression on all affected APIs and microservices, "
                        + "followed by targeted integration tests for impacted transaction flows. "
                        + "Compliance controls require attestation testing before production release.")
                .deploymentStrategy("Phased canary deployment: deploy database changes first, then microservices, "
                        + "then API gateway routes, then application UI updates. "
                        + "Use feature flags for high-risk components and coordinate with operations teams.")
                .rollbackStrategy("Maintain versioned rollback artifacts for each affected microservice and API. "
                        + "Database migrations must include down scripts. "
                        + "If production validation fails, revert canary traffic and roll back to previous stable version.")
                .productionValidationChecklist(List.of(
                        "All user stories marked DONE with acceptance criteria verified",
                        "Smoke tests pass on affected APIs: " + String.join(", ", extractNames(impacts, ComponentType.API)),
                        "Sanctions and compliance controls validated in production",
                        "Monitoring dashboards show no elevated error rates post-deployment",
                        "Runbook updated for operational teams",
                        "Executive compliance sign-off obtained",
                        "Rollback drill completed successfully in staging"))
                .build();
    }

    private List<ImpactItemSchema> collectAllImpacts(EnterpriseImpactAssessmentSchema assessment) {
        return Stream.of(
                        assessment.getApplicationImpacts(),
                        assessment.getMicroserviceImpacts(),
                        assessment.getApiImpacts(),
                        assessment.getDatabaseImpacts(),
                        assessment.getBusinessTeamImpacts(),
                        assessment.getCustomerImpacts(),
                        assessment.getTransactionImpacts(),
                        assessment.getOperationalImpacts(),
                        assessment.getEngineeringImpacts(),
                        assessment.getComplianceRiskImpacts())
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(ImpactItemSchema::getSeverity).reversed())
                .toList();
    }

    private Map<ImpactCategory, List<ImpactItemSchema>> groupByCategory(List<ImpactItemSchema> impacts) {
        Map<ImpactCategory, List<ImpactItemSchema>> grouped = new LinkedHashMap<>();
        for (ImpactCategory category : ImpactCategory.values()) {
            grouped.put(category, new ArrayList<>());
        }
        for (ImpactItemSchema impact : impacts) {
            grouped.get(impact.getCategory()).add(impact);
        }
        grouped.entrySet().removeIf(e -> e.getValue().isEmpty());
        return grouped;
    }

    private List<String> extractComponentNames(List<ImpactItemSchema> impacts, ComponentType type) {
        return impacts.stream()
                .filter(i -> i.getComponentType() == type)
                .map(ImpactItemSchema::getComponentName)
                .distinct()
                .toList();
    }

    private List<String> extractNames(List<ImpactItemSchema> impacts, ComponentType type) {
        List<String> names = extractComponentNames(impacts, type);
        return names.isEmpty() ? List.of("N/A") : names;
    }

    private Priority highestPriority(List<ImpactItemSchema> impacts) {
        return impacts.stream()
                .map(i -> mapSeverityToPriority(i.getSeverity()))
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(Priority.MEDIUM);
    }

    private Priority mapSeverityToPriority(ImpactSeverity severity) {
        return switch (severity) {
            case CRITICAL -> Priority.CRITICAL;
            case HIGH -> Priority.HIGH;
            case LOW -> Priority.LOW;
            default -> Priority.MEDIUM;
        };
    }

    private int mapSeverityToStoryPoints(ImpactSeverity severity) {
        return switch (severity) {
            case CRITICAL -> 13;
            case HIGH -> 8;
            case MEDIUM -> 5;
            case LOW -> 3;
        };
    }

    private String resolveOwnerTeam(ImpactItemSchema impact) {
        String name = impact.getComponentName().toLowerCase(Locale.ROOT);
        return switch (impact.getCategory()) {
            case APPLICATION, MICROSERVICE, API -> name.contains("trade") || name.contains("finance")
                    ? "Trade Finance Engineering"
                    : "Payment Platform Engineering";
            case DATABASE -> "Platform Data Engineering";
            case BUSINESS_TEAM, OPERATIONAL -> "Compliance Operations";
            case CUSTOMER, TRANSACTION -> "Product Operations";
            case ENGINEERING -> impact.getComponentName();
            case COMPLIANCE_RISK -> "Compliance Engineering";
        };
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength) + "...";
    }
}
