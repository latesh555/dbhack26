package com.regintel.ai.regintel.agent;

import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.enterpriseimpact.schema.ImpactItemSchema;
import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import com.regintel.ai.engineeringplanning.schema.UserStorySchema;
import com.regintel.ai.regintel.schema.ExecutiveDecisionReportSchema;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ExecutiveDecisionCopilotAgent {

    public ExecutiveDecisionReportSchema generate(
            RegulatoryIntelligence intelligence,
            EnterpriseImpactAssessmentSchema impact,
            EngineeringDeliveryPlanSchema plan) {

        int totalImpacts = countTotalImpacts(impact);
        int totalStories = countUserStories(plan);
        int totalPoints = plan.getEpic() != null && plan.getEpic().getTotalStoryPoints() != null
                ? plan.getEpic().getTotalStoryPoints()
                : 0;

        BigDecimal riskScore = calculateRiskScore(intelligence, impact);

        return ExecutiveDecisionReportSchema.builder()
                .executiveSummary(buildExecutiveSummary(intelligence, totalImpacts, totalStories, riskScore))
                .whatChanged(buildWhatChanged(intelligence))
                .whyItMatters(buildWhyItMatters(intelligence, totalImpacts))
                .enterpriseImpact(buildEnterpriseImpact(impact, totalImpacts))
                .customerImpact(buildCustomerImpact(impact))
                .businessImpact(buildBusinessImpact(intelligence, impact))
                .operationalImpact(buildOperationalImpact(impact))
                .engineeringReadiness(buildEngineeringReadiness(plan, totalStories, totalPoints))
                .keyRisks(buildKeyRisks(intelligence, impact))
                .recommendations(buildRecommendations(intelligence, impact, plan))
                .immediateActions(buildImmediateActions(intelligence, impact))
                .leadershipDecisionsRequired(buildLeadershipDecisions(intelligence, plan, riskScore))
                .overallRiskScore(riskScore)
                .implementationTimeline(buildTimeline(plan, totalPoints))
                .build();
    }

    private String buildExecutiveSummary(
            RegulatoryIntelligence intelligence, int totalImpacts, int totalStories, BigDecimal riskScore) {
        return intelligence.getRegulatoryAuthority() + " has issued a " + intelligence.getDocumentType()
                + " with " + intelligence.getSeverity() + " severity affecting " + intelligence.getJurisdictions()
                + ". Our analysis identified " + totalImpacts + " enterprise impacts and generated "
                + totalStories + " engineering user stories. Overall risk score: " + riskScore + "/100.";
    }

    private String buildWhatChanged(RegulatoryIntelligence intelligence) {
        StringBuilder sb = new StringBuilder();
        sb.append(intelligence.getNewRequirements().size()).append(" new regulatory requirement(s) identified. ");
        if (intelligence.getSanctionsInformation() != null) {
            sb.append("Sanctions: ").append(intelligence.getSanctionsInformation()).append(" ");
        }
        intelligence.getKeyChanges().forEach(c ->
                sb.append("[").append(c.getChangeType()).append("] ").append(c.getSummary()).append(". "));
        return sb.toString().trim();
    }

    private String buildWhyItMatters(RegulatoryIntelligence intelligence, int totalImpacts) {
        return "This regulation affects " + String.join(", ", intelligence.getIndustries())
                + " operations across " + String.join(", ", intelligence.getBusinessDomains())
                + " with " + totalImpacts + " confirmed enterprise component impacts. "
                + "Non-compliance exposes the organization to regulatory penalties and operational disruption.";
    }

    private String buildEnterpriseImpact(EnterpriseImpactAssessmentSchema impact, int totalImpacts) {
        return totalImpacts + " components impacted across applications, microservices, APIs, databases, "
                + "and compliance controls. Overall heatmap risk: "
                + (impact.getRiskHeatmap() != null ? impact.getRiskHeatmap().getOverallRisk() : "MEDIUM")
                + " with " + (impact.getRiskHeatmap() != null ? impact.getRiskHeatmap().getCriticalCount() : 0)
                + " critical impact(s).";
    }

    private String buildCustomerImpact(EnterpriseImpactAssessmentSchema impact) {
        List<ImpactItemSchema> customers = impact.getCustomerImpacts();
        if (customers.isEmpty()) {
            return "No direct customer segment impacts identified.";
        }
        return customers.size() + " customer segment(s) affected: "
                + String.join(", ", customers.stream().map(ImpactItemSchema::getComponentName).toList());
    }

    private String buildBusinessImpact(
            RegulatoryIntelligence intelligence, EnterpriseImpactAssessmentSchema impact) {
        return "Business units in " + String.join(", ", intelligence.getBusinessDomains())
                + " require process updates. " + impact.getBusinessTeamImpacts().size()
                + " business team(s) directly impacted. Compliance driver: "
                + (impact.getComplianceRisk() != null
                ? impact.getComplianceRisk().getPrimaryRegulatoryDriver()
                : intelligence.getRegulatoryAuthority());
    }

    private String buildOperationalImpact(EnterpriseImpactAssessmentSchema impact) {
        return impact.getOperationalImpacts().size() + " operational process(es) require updates. "
                + impact.getBusinessTeamImpacts().size() + " team(s) need operational readiness planning.";
    }

    private String buildEngineeringReadiness(
            EngineeringDeliveryPlanSchema plan, int totalStories, int totalPoints) {
        String jiraRef = plan.getJiraSyncReference() != null ? plan.getJiraSyncReference() : "pending";
        return "Engineering delivery plan generated with " + totalStories + " user stories ("
                + totalPoints + " story points). Jira sync: " + jiraRef + ". "
                + "Affected APIs: " + plan.getAffectedApis().size()
                + ", microservices: " + plan.getAffectedMicroservices().size() + ".";
    }

    private List<String> buildKeyRisks(
            RegulatoryIntelligence intelligence, EnterpriseImpactAssessmentSchema impact) {
        List<String> risks = new ArrayList<>();
        risks.add("Regulatory non-compliance penalty risk (" + intelligence.getSeverity() + " severity)");
        if (intelligence.getSanctionsInformation() != null) {
            risks.add("Sanctions screening failure risk for cross-border transactions");
        }
        if (impact.getRiskHeatmap() != null && impact.getRiskHeatmap().getCriticalCount() > 0) {
            risks.add(impact.getRiskHeatmap().getCriticalCount() + " critical enterprise component impact(s)");
        }
        intelligence.getDeadlines().forEach(d ->
                risks.add("Deadline risk: " + d.getDescription() + " by " + d.getDueDate()));
        return risks;
    }

    private List<String> buildRecommendations(
            RegulatoryIntelligence intelligence,
            EnterpriseImpactAssessmentSchema impact,
            EngineeringDeliveryPlanSchema plan) {
        List<String> recs = new ArrayList<>();
        recs.add("Establish cross-functional war room with Compliance and Engineering leads");
        recs.add("Prioritize CRITICAL and HIGH severity impacts for immediate sprint planning");
        if (plan.getStrategies() != null) {
            recs.add("Follow phased deployment: " + truncate(plan.getStrategies().getDeploymentStrategy(), 120));
        }
        recs.add("Conduct compliance attestation before production release");
        recs.add("Brief " + String.join("/", intelligence.getJurisdictions()) + " business stakeholders");
        return recs;
    }

    private List<String> buildImmediateActions(
            RegulatoryIntelligence intelligence, EnterpriseImpactAssessmentSchema impact) {
        List<String> actions = new ArrayList<>();
        actions.add("Review and approve enterprise impact assessment");
        actions.add("Assign engineering squad owners for top " + Math.min(5, countTotalImpacts(impact)) + " impacts");
        if (intelligence.getSanctionsInformation() != null) {
            actions.add("Activate sanctions screening control validation within 48 hours");
        }
        actions.add("Schedule leadership review within 5 business days");
        intelligence.getDeadlines().stream().findFirst().ifPresent(d ->
                actions.add("Confirm readiness plan for deadline: " + d.getDueDate()));
        return actions;
    }

    private List<String> buildLeadershipDecisions(
            RegulatoryIntelligence intelligence,
            EngineeringDeliveryPlanSchema plan,
            BigDecimal riskScore) {
        List<String> decisions = new ArrayList<>();
        decisions.add("Approve engineering delivery budget for " + countUserStories(plan) + " user stories");
        if (riskScore.compareTo(new BigDecimal("75")) >= 0) {
            decisions.add("Authorize accelerated compliance track given high risk score");
        }
        decisions.add("Confirm regulatory interpretation with legal counsel");
        decisions.add("Approve customer communication strategy for affected segments");
        decisions.add("Set executive sponsor for " + intelligence.getRegulatoryAuthority() + " compliance program");
        return decisions;
    }

    private BigDecimal calculateRiskScore(
            RegulatoryIntelligence intelligence, EnterpriseImpactAssessmentSchema impact) {
        double score = 40.0;
        score += switch (intelligence.getSeverity().toUpperCase(Locale.ROOT)) {
            case "CRITICAL" -> 30;
            case "HIGH" -> 20;
            case "MEDIUM" -> 10;
            default -> 5;
        };
        if (impact.getRiskHeatmap() != null) {
            score += impact.getRiskHeatmap().getCriticalCount() * 5.0;
            score += impact.getRiskHeatmap().getHighCount() * 2.0;
        }
        score += Math.min(intelligence.getNewRequirements().size() * 2.0, 10.0);
        return BigDecimal.valueOf(Math.min(score, 100.0)).setScale(1, RoundingMode.HALF_UP);
    }

    private String buildTimeline(EngineeringDeliveryPlanSchema plan, int totalPoints) {
        int weeks = Math.max(2, (int) Math.ceil(totalPoints / 20.0));
        return "Estimated " + weeks + " week(s) across " + countUserStories(plan)
                + " user stories (" + totalPoints + " story points). "
                + "Phase 1 (weeks 1-" + Math.max(1, weeks / 3) + "): database and compliance controls. "
                + "Phase 2: microservices and APIs. Phase 3: applications and production validation.";
    }

    private int countTotalImpacts(EnterpriseImpactAssessmentSchema impact) {
        return impact.getApplicationImpacts().size()
                + impact.getMicroserviceImpacts().size()
                + impact.getApiImpacts().size()
                + impact.getDatabaseImpacts().size()
                + impact.getBusinessTeamImpacts().size()
                + impact.getCustomerImpacts().size()
                + impact.getTransactionImpacts().size()
                + impact.getOperationalImpacts().size()
                + impact.getEngineeringImpacts().size()
                + impact.getComplianceRiskImpacts().size();
    }

    private int countUserStories(EngineeringDeliveryPlanSchema plan) {
        if (plan.getEpic() == null) {
            return 0;
        }
        return plan.getEpic().getFeatures().stream()
                .mapToInt(f -> f.getUserStories().size())
                .sum();
    }

    private String truncate(String value, int max) {
        if (value == null) {
            return "";
        }
        return value.length() <= max ? value : value.substring(0, max) + "...";
    }
}
