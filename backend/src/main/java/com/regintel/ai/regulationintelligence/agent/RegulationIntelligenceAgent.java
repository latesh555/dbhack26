package com.regintel.ai.regulationintelligence.agent;

import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulationintelligence.schema.RegulatoryDeadline;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import com.regintel.ai.regulationintelligence.schema.RegulatoryRequirement;
import com.regintel.ai.regulationintelligence.schema.RequirementChange;
import com.regintel.ai.regulationintelligence.schema.SourceReference;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class RegulationIntelligenceAgent {

    public RegulatoryIntelligence analyze(Regulation regulation) {
        String content = regulation.getRawContent() != null
                ? regulation.getRawContent().toLowerCase(Locale.ROOT)
                : "";
        String jurisdiction = regulation.getJurisdiction();
        String severity = deriveSeverity(content);

        List<String> industries = deriveIndustries(content);
        List<String> businessDomains = deriveBusinessDomains(content);
        List<RegulatoryRequirement> newRequirements = deriveRequirements(content, regulation.getTitle());
        List<RequirementChange> keyChanges = deriveKeyChanges(newRequirements);

        return RegulatoryIntelligence.builder()
                .documentType(regulation.getDocumentType())
                .regulatoryAuthority(regulation.getSource())
                .publicationDate(LocalDate.now())
                .effectiveDate(regulation.getEffectiveDate())
                .severity(severity)
                .executiveSummary(buildExecutiveSummary(regulation, newRequirements.size()))
                .keyChanges(keyChanges)
                .newRequirements(newRequirements)
                .removedRequirements(List.of())
                .modifiedRequirements(List.of())
                .deadlines(deriveDeadlines(regulation))
                .jurisdictions(List.of(jurisdiction))
                .industries(industries)
                .sanctionsInformation(deriveSanctionsInfo(content))
                .businessDomains(businessDomains)
                .build();
    }

    private String buildExecutiveSummary(Regulation regulation, int requirementCount) {
        return "Regulatory document '" + regulation.getTitle() + "' from " + regulation.getSource()
                + " introduces " + requirementCount + " new requirement(s) affecting "
                + regulation.getJurisdiction() + " operations. Immediate enterprise review is recommended.";
    }

    private List<RegulatoryRequirement> deriveRequirements(String content, String title) {
        List<RegulatoryRequirement> requirements = new ArrayList<>();
        int idx = 1;

        if (containsAny(content, "sanction", "ofac", "embargo", "watchlist")) {
            requirements.add(requirement("REQ-" + idx++, "Enhanced sanctions screening",
                    "All cross-border payment and trade finance transactions must pass real-time sanctions screening.",
                    "Article 4.2", "Payment institutions shall screen transfers against updated sanctions lists."));
        }
        if (containsAny(content, "payment", "swift", "transfer", "settlement")) {
            requirements.add(requirement("REQ-" + idx++, "Payment transaction reporting",
                    "Payment processors must maintain enhanced audit trails for cross-border transfers.",
                    "Article 5.1", "Payment records must be retained for regulatory audit purposes."));
        }
        if (containsAny(content, "trade", "letter of credit", "lc", "documentary", "import", "export")) {
            requirements.add(requirement("REQ-" + idx++, "Trade finance compliance",
                    "Trade finance workflows require updated documentary compliance checks.",
                    "Article 7.1", "Documentary trade products require compliance attestation."));
        }
        if (containsAny(content, "kyc", "customer", "onboarding", "cdd", "identity")) {
            requirements.add(requirement("REQ-" + idx++, "Customer due diligence enhancement",
                    "Customer onboarding must include enhanced due diligence for high-risk jurisdictions.",
                    "Article 3.3", "CDD procedures must be updated for corporate customers."));
        }
        if (containsAny(content, "aml", "anti-money", "suspicious", "monitoring")) {
            requirements.add(requirement("REQ-" + idx++, "AML transaction monitoring",
                    "Transaction monitoring thresholds must be recalibrated per updated regulatory guidance.",
                    "Article 6.4", "Suspicious activity detection rules require recalibration."));
        }
        if (requirements.isEmpty()) {
            requirements.add(requirement("REQ-001", "General regulatory compliance",
                    "Enterprise systems must align with requirements defined in: " + title,
                    "Section 1", content.length() > 200 ? content.substring(0, 200) : content));
        }
        return requirements;
    }

    private RegulatoryRequirement requirement(
            String id, String title, String description, String section, String supportingText) {
        return RegulatoryRequirement.builder()
                .requirementId(id)
                .title(title)
                .description(description)
                .sourceReference(SourceReference.builder()
                        .pageNumber(1)
                        .section(section)
                        .supportingText(supportingText)
                        .confidenceScore(0.92)
                        .build())
                .build();
    }

    private List<RequirementChange> deriveKeyChanges(List<RegulatoryRequirement> requirements) {
        return requirements.stream()
                .map(r -> RequirementChange.builder()
                        .changeId("CHG-" + r.getRequirementId())
                        .summary(r.getTitle())
                        .changeType("NEW")
                        .sourceReference(r.getSourceReference())
                        .build())
                .toList();
    }

    private List<RegulatoryDeadline> deriveDeadlines(Regulation regulation) {
        LocalDate effective = regulation.getEffectiveDate() != null
                ? regulation.getEffectiveDate()
                : LocalDate.now().plusMonths(6);
        return List.of(
                RegulatoryDeadline.builder()
                        .description("Compliance implementation deadline")
                        .dueDate(effective)
                        .affectedArea("Enterprise-wide")
                        .build(),
                RegulatoryDeadline.builder()
                        .description("Internal readiness review")
                        .dueDate(effective.minusMonths(1))
                        .affectedArea("Compliance & Engineering")
                        .build()
        );
    }

    private String deriveSanctionsInfo(String content) {
        if (containsAny(content, "sanction", "ofac", "embargo")) {
            return "Updated sanctions list screening requirements apply to all cross-border payment "
                    + "and trade finance activities. OFAC-aligned screening recalibration required.";
        }
        return null;
    }

    private String deriveSeverity(String content) {
        if (containsAny(content, "sanction", "critical", "mandatory", "immediate")) {
            return "CRITICAL";
        }
        if (containsAny(content, "payment", "aml", "compliance", "required")) {
            return "HIGH";
        }
        return "MEDIUM";
    }

    private List<String> deriveIndustries(String content) {
        List<String> industries = new ArrayList<>();
        industries.add("Banking");
        if (containsAny(content, "trade", "finance", "lc")) {
            industries.add("Trade Finance");
        }
        if (containsAny(content, "payment", "swift")) {
            industries.add("Payments");
        }
        return industries;
    }

    private List<String> deriveBusinessDomains(String content) {
        List<String> domains = new ArrayList<>();
        domains.add("Compliance");
        if (containsAny(content, "payment", "swift", "transfer")) {
            domains.add("Payments");
        }
        if (containsAny(content, "trade", "lc", "documentary")) {
            domains.add("Trade Finance");
        }
        if (containsAny(content, "customer", "kyc")) {
            domains.add("Customer Lifecycle");
        }
        return domains;
    }

    private boolean containsAny(String content, String... keywords) {
        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
