package com.regintel.ai.enterpriseimpact.agent;

import com.regintel.ai.enterpriseimpact.entity.ComponentType;
import com.regintel.ai.enterpriseimpact.entity.ImpactCategory;
import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import com.regintel.ai.enterpriseimpact.knowledge.KnowledgeBaseComponent;
import com.regintel.ai.enterpriseimpact.knowledge.MockEnterpriseKnowledgeBase;
import com.regintel.ai.enterpriseimpact.schema.ComplianceRiskSchema;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.enterpriseimpact.schema.ImpactItemSchema;
import com.regintel.ai.enterpriseimpact.schema.RiskHeatmapSchema;
import com.regintel.ai.regulationintelligence.schema.RegulatoryDeadline;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import com.regintel.ai.regulationintelligence.schema.RegulatoryRequirement;
import com.regintel.ai.regulationintelligence.schema.SourceReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class EnterpriseImpactAnalystAgent {

    private static final BigDecimal MIN_CONFIDENCE = new BigDecimal("0.5500");

    private final MockEnterpriseKnowledgeBase knowledgeBase;

    public EnterpriseImpactAssessmentSchema analyze(RegulatoryIntelligence intelligence) {
        String corpus = buildCorpus(intelligence);
        Set<String> corpusTokens = tokenize(corpus);

        List<ImpactItemSchema> allImpacts = new ArrayList<>();

        for (KnowledgeBaseComponent component : knowledgeBase.getAllComponents()) {
            MatchResult match = scoreComponent(component, intelligence, corpus, corpusTokens);
            if (match.score() >= 0.55) {
                allImpacts.add(buildImpactItem(component, match, intelligence));
            }
        }

        allImpacts.addAll(buildDeadlineOperationalImpacts(intelligence));
        allImpacts.addAll(buildSanctionsImpacts(intelligence));
        allImpacts = deduplicateImpacts(allImpacts);

        ComplianceRiskSchema complianceRisk = buildComplianceRisk(intelligence, allImpacts);
        RiskHeatmapSchema heatmap = buildRiskHeatmap(allImpacts, intelligence);

        return EnterpriseImpactAssessmentSchema.builder()
                .applicationImpacts(filterByCategory(allImpacts, ImpactCategory.APPLICATION))
                .microserviceImpacts(filterByCategory(allImpacts, ImpactCategory.MICROSERVICE))
                .apiImpacts(filterByCategory(allImpacts, ImpactCategory.API))
                .databaseImpacts(filterByCategory(allImpacts, ImpactCategory.DATABASE))
                .businessTeamImpacts(filterByCategory(allImpacts, ImpactCategory.BUSINESS_TEAM))
                .customerImpacts(filterByCategory(allImpacts, ImpactCategory.CUSTOMER))
                .transactionImpacts(filterByCategory(allImpacts, ImpactCategory.TRANSACTION))
                .operationalImpacts(filterByCategory(allImpacts, ImpactCategory.OPERATIONAL))
                .engineeringImpacts(filterByCategory(allImpacts, ImpactCategory.ENGINEERING))
                .complianceRiskImpacts(filterByCategory(allImpacts, ImpactCategory.COMPLIANCE_RISK))
                .complianceRisk(complianceRisk)
                .riskHeatmap(heatmap)
                .build();
    }

    private MatchResult scoreComponent(
            KnowledgeBaseComponent component,
            RegulatoryIntelligence intelligence,
            String corpus,
            Set<String> corpusTokens) {

        double tagScore = component.getTags().stream()
                .map(this::tokenize)
                .flatMap(Collection::stream)
                .filter(corpusTokens::contains)
                .count();
        double tagRatio = component.getTags().isEmpty() ? 0 : tagScore / component.getTags().size();

        double jurisdictionScore = overlapRatio(component.getJurisdictions(), intelligence.getJurisdictions());
        double industryScore = overlapRatio(component.getIndustries(), intelligence.getIndustries());
        double domainScore = overlapRatio(component.getBusinessDomains(), intelligence.getBusinessDomains());

        double requirementBoost = countRequirementMatches(component, intelligence) * 0.08;
        double severityBoost = mapSeverityBoost(intelligence.getSeverity());

        double score = (tagRatio * 0.45) + (jurisdictionScore * 0.20) + (industryScore * 0.15)
                + (domainScore * 0.15) + requirementBoost + severityBoost;

        List<String> matchedTags = component.getTags().stream()
                .filter(tag -> corpus.toLowerCase(Locale.ROOT).contains(tag.toLowerCase(Locale.ROOT)))
                .toList();

        return new MatchResult(
                Math.min(score, 1.0),
                matchedTags,
                pickEvidence(intelligence, matchedTags));
    }

    private ImpactItemSchema buildImpactItem(
            KnowledgeBaseComponent component,
            MatchResult match,
            RegulatoryIntelligence intelligence) {

        ImpactSeverity severity = deriveSeverity(intelligence.getSeverity(), match.score());
        BigDecimal confidence = BigDecimal.valueOf(match.score()).setScale(4, RoundingMode.HALF_UP);

        String reason = buildReason(component, intelligence, match);
        String evidence = match.evidence() != null ? match.evidence() : component.getDescription();

        return ImpactItemSchema.builder()
                .componentName(component.getName())
                .componentType(component.getComponentType())
                .category(component.getCategory())
                .reason(reason)
                .severity(severity)
                .confidence(confidence.max(MIN_CONFIDENCE))
                .evidence(evidence)
                .build();
    }

    private List<ImpactItemSchema> buildDeadlineOperationalImpacts(RegulatoryIntelligence intelligence) {
        if (intelligence.getDeadlines() == null || intelligence.getDeadlines().isEmpty()) {
            return List.of();
        }
        List<ImpactItemSchema> impacts = new ArrayList<>();
        for (RegulatoryDeadline deadline : intelligence.getDeadlines()) {
            impacts.add(ImpactItemSchema.builder()
                    .componentName("Regulatory Reporting Pipeline")
                    .componentType(ComponentType.PROCESS)
                    .category(ImpactCategory.OPERATIONAL)
                    .reason("Regulatory deadline requires operational readiness: " + deadline.getDescription())
                    .severity(ImpactSeverity.HIGH)
                    .confidence(new BigDecimal("0.9000"))
                    .evidence("Deadline due " + deadline.getDueDate() + " affecting " + deadline.getAffectedArea())
                    .build());

            impacts.add(ImpactItemSchema.builder()
                    .componentName("Compliance Team")
                    .componentType(ComponentType.BUSINESS_TEAM)
                    .category(ImpactCategory.BUSINESS_TEAM)
                    .reason("Compliance team must coordinate deadline: " + deadline.getDescription())
                    .severity(ImpactSeverity.HIGH)
                    .confidence(new BigDecimal("0.8800"))
                    .evidence("Deadline due " + deadline.getDueDate())
                    .build());
        }
        return impacts;
    }

    private List<ImpactItemSchema> buildSanctionsImpacts(RegulatoryIntelligence intelligence) {
        if (intelligence.getSanctionsInformation() == null || intelligence.getSanctionsInformation().isBlank()) {
            return List.of();
        }
        String sanctions = intelligence.getSanctionsInformation();
        return List.of(
                ImpactItemSchema.builder()
                        .componentName("sanctions-checker")
                        .componentType(ComponentType.MICROSERVICE)
                        .category(ImpactCategory.MICROSERVICE)
                        .reason("Sanctions regulatory changes require screening logic updates")
                        .severity(ImpactSeverity.CRITICAL)
                        .confidence(new BigDecimal("0.9500"))
                        .evidence(sanctions)
                        .build(),
                ImpactItemSchema.builder()
                        .componentName("Sanctions Compliance Control")
                        .componentType(ComponentType.COMPLIANCE_CONTROL)
                        .category(ImpactCategory.COMPLIANCE_RISK)
                        .reason("Sanctions compliance control must align with new regulatory requirements")
                        .severity(ImpactSeverity.CRITICAL)
                        .confidence(new BigDecimal("0.9300"))
                        .evidence(sanctions)
                        .build(),
                ImpactItemSchema.builder()
                        .componentName("Sanctions Screening Workflow")
                        .componentType(ComponentType.PROCESS)
                        .category(ImpactCategory.OPERATIONAL)
                        .reason("Operational sanctions screening workflow requires process updates")
                        .severity(ImpactSeverity.HIGH)
                        .confidence(new BigDecimal("0.9000"))
                        .evidence(sanctions)
                        .build()
        );
    }

    private ComplianceRiskSchema buildComplianceRisk(
            RegulatoryIntelligence intelligence,
            List<ImpactItemSchema> impacts) {

        long complianceImpacts = impacts.stream()
                .filter(i -> i.getCategory() == ImpactCategory.COMPLIANCE_RISK)
                .count();

        ImpactSeverity overall = mapRegulationSeverity(intelligence.getSeverity());
        if (complianceImpacts >= 3) {
            overall = ImpactSeverity.CRITICAL;
        } else if (complianceImpacts >= 1 && overall.ordinal() < ImpactSeverity.HIGH.ordinal()) {
            overall = ImpactSeverity.HIGH;
        }

        return ComplianceRiskSchema.builder()
                .summary("Regulation from " + intelligence.getRegulatoryAuthority()
                        + " introduces " + complianceImpacts + " direct compliance control impacts across "
                        + intelligence.getJurisdictions().size() + " jurisdiction(s).")
                .overallSeverity(overall)
                .affectedControls((int) complianceImpacts)
                .primaryRegulatoryDriver(intelligence.getRegulatoryAuthority())
                .build();
    }

    private RiskHeatmapSchema buildRiskHeatmap(
            List<ImpactItemSchema> impacts,
            RegulatoryIntelligence intelligence) {

        Map<String, ImpactSeverity> domainSeverity = new LinkedHashMap<>();
        for (String domain : intelligence.getBusinessDomains()) {
            domainSeverity.put(domain, mapRegulationSeverity(intelligence.getSeverity()));
        }
        if (domainSeverity.isEmpty()) {
            intelligence.getIndustries().forEach(industry ->
                    domainSeverity.put(industry, mapRegulationSeverity(intelligence.getSeverity())));
        }

        int critical = countBySeverity(impacts, ImpactSeverity.CRITICAL);
        int high = countBySeverity(impacts, ImpactSeverity.HIGH);
        int medium = countBySeverity(impacts, ImpactSeverity.MEDIUM);
        int low = countBySeverity(impacts, ImpactSeverity.LOW);

        ImpactSeverity overall = ImpactSeverity.LOW;
        if (critical > 0) {
            overall = ImpactSeverity.CRITICAL;
        } else if (high > 0) {
            overall = ImpactSeverity.HIGH;
        } else if (medium > 0) {
            overall = ImpactSeverity.MEDIUM;
        }

        return RiskHeatmapSchema.builder()
                .domainSeverity(domainSeverity)
                .totalImpacts(impacts.size())
                .criticalCount(critical)
                .highCount(high)
                .mediumCount(medium)
                .lowCount(low)
                .overallRisk(overall)
                .build();
    }

    private List<ImpactItemSchema> deduplicateImpacts(List<ImpactItemSchema> impacts) {
        Map<String, ImpactItemSchema> unique = new LinkedHashMap<>();
        for (ImpactItemSchema impact : impacts) {
            String key = impact.getCategory() + "::" + impact.getComponentName();
            unique.merge(key, impact, (existing, incoming) ->
                    incoming.getConfidence().compareTo(existing.getConfidence()) > 0 ? incoming : existing);
        }
        return new ArrayList<>(unique.values());
    }

    private List<ImpactItemSchema> filterByCategory(List<ImpactItemSchema> impacts, ImpactCategory category) {
        return impacts.stream()
                .filter(i -> i.getCategory() == category)
                .sorted(Comparator.comparing(ImpactItemSchema::getSeverity).reversed())
                .toList();
    }

    private String buildCorpus(RegulatoryIntelligence intelligence) {
        return Stream.of(
                        intelligence.getExecutiveSummary(),
                        intelligence.getSanctionsInformation(),
                        String.join(" ", safeList(intelligence.getJurisdictions())),
                        String.join(" ", safeList(intelligence.getIndustries())),
                        String.join(" ", safeList(intelligence.getBusinessDomains())),
                        intelligence.getNewRequirements().stream()
                                .map(RegulatoryRequirement::getDescription).collect(Collectors.joining(" ")),
                        intelligence.getModifiedRequirements().stream()
                                .map(RegulatoryRequirement::getDescription).collect(Collectors.joining(" ")),
                        intelligence.getKeyChanges().stream()
                                .map(c -> c.getSummary()).collect(Collectors.joining(" ")))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "))
                .toLowerCase(Locale.ROOT);
    }

    private int countRequirementMatches(KnowledgeBaseComponent component, RegulatoryIntelligence intelligence) {
        int matches = 0;
        List<RegulatoryRequirement> requirements = new ArrayList<>();
        requirements.addAll(intelligence.getNewRequirements());
        requirements.addAll(intelligence.getModifiedRequirements());

        for (RegulatoryRequirement requirement : requirements) {
            String text = (requirement.getTitle() + " " + requirement.getDescription()).toLowerCase(Locale.ROOT);
            for (String tag : component.getTags()) {
                if (text.contains(tag.toLowerCase(Locale.ROOT))) {
                    matches++;
                    break;
                }
            }
        }
        return matches;
    }

    private String buildReason(KnowledgeBaseComponent component, RegulatoryIntelligence intelligence, MatchResult match) {
        String matched = match.matchedTags().isEmpty()
                ? "regulatory scope overlap"
                : "matched regulatory themes: " + String.join(", ", match.matchedTags());
        return component.getName() + " is impacted because " + matched + " under "
                + intelligence.getRegulatoryAuthority() + " (" + intelligence.getDocumentType() + ").";
    }

    private String pickEvidence(RegulatoryIntelligence intelligence, List<String> matchedTags) {
        Optional<RegulatoryRequirement> requirement = Stream.concat(
                        intelligence.getNewRequirements().stream(),
                        intelligence.getModifiedRequirements().stream())
                .filter(r -> matchedTags.stream().anyMatch(tag ->
                        r.getDescription().toLowerCase(Locale.ROOT).contains(tag.toLowerCase(Locale.ROOT))
                                || r.getTitle().toLowerCase(Locale.ROOT).contains(tag.toLowerCase(Locale.ROOT))))
                .findFirst();

        if (requirement.isPresent() && requirement.get().getSourceReference() != null) {
            SourceReference ref = requirement.get().getSourceReference();
            return "Section " + ref.getSection() + " (page " + ref.getPageNumber() + "): "
                    + ref.getSupportingText();
        }
        return intelligence.getExecutiveSummary();
    }

    private ImpactSeverity deriveSeverity(String regulationSeverity, double matchScore) {
        ImpactSeverity base = mapRegulationSeverity(regulationSeverity);
        if (matchScore >= 0.85 && base.ordinal() < ImpactSeverity.HIGH.ordinal()) {
            return ImpactSeverity.HIGH;
        }
        if (matchScore >= 0.70 && base.ordinal() < ImpactSeverity.MEDIUM.ordinal()) {
            return ImpactSeverity.MEDIUM;
        }
        return base;
    }

    private ImpactSeverity mapRegulationSeverity(String severity) {
        if (severity == null) {
            return ImpactSeverity.MEDIUM;
        }
        return switch (severity.toUpperCase(Locale.ROOT)) {
            case "CRITICAL" -> ImpactSeverity.CRITICAL;
            case "HIGH" -> ImpactSeverity.HIGH;
            case "LOW" -> ImpactSeverity.LOW;
            default -> ImpactSeverity.MEDIUM;
        };
    }

    private double mapSeverityBoost(String severity) {
        return switch (mapRegulationSeverity(severity)) {
            case CRITICAL -> 0.10;
            case HIGH -> 0.06;
            case MEDIUM -> 0.03;
            case LOW -> 0.01;
        };
    }

    private double overlapRatio(List<String> left, List<String> right) {
        if (left.isEmpty() || right.isEmpty()) {
            return 0;
        }
        Set<String> normalizedLeft = left.stream()
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
        long overlap = right.stream()
                .map(s -> s.toLowerCase(Locale.ROOT))
                .filter(normalizedLeft::contains)
                .count();
        return (double) overlap / left.size();
    }

    private Set<String> tokenize(String text) {
        if (text == null || text.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(text.toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
                .filter(token -> token.length() > 2)
                .collect(Collectors.toSet());
    }

    private int countBySeverity(List<ImpactItemSchema> impacts, ImpactSeverity severity) {
        return (int) impacts.stream().filter(i -> i.getSeverity() == severity).count();
    }

    private List<String> safeList(List<String> values) {
        return values != null ? values : List.of();
    }

    private record MatchResult(double score, List<String> matchedTags, String evidence) {
    }
}
