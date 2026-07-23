package com.regintel.ai.enterpriseimpact.agent;

import com.regintel.ai.enterpriseimpact.knowledge.KnowledgeBaseComponent;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;

import java.util.List;
import java.util.stream.Collectors;

final class EnterpriseImpactPromptBuilder {

    private EnterpriseImpactPromptBuilder() {
    }

    static String systemPrompt() {
        return """
                You are an enterprise impact analyst for a global bank. Given structured regulatory \
                intelligence and an enterprise component inventory, identify ALL impacted applications, \
                microservices, APIs, databases, teams, customers, transactions, operational processes, \
                engineering squads, and compliance controls.

                Return ONLY valid JSON (no markdown) matching this schema:

                {
                  "applicationImpacts": [impactItem],
                  "microserviceImpacts": [impactItem],
                  "apiImpacts": [impactItem],
                  "databaseImpacts": [impactItem],
                  "businessTeamImpacts": [impactItem],
                  "customerImpacts": [impactItem],
                  "transactionImpacts": [impactItem],
                  "operationalImpacts": [impactItem],
                  "engineeringImpacts": [impactItem],
                  "complianceRiskImpacts": [impactItem],
                  "complianceRisk": {
                    "summary": "string",
                    "overallSeverity": "CRITICAL|HIGH|MEDIUM|LOW",
                    "affectedControls": 0,
                    "primaryRegulatoryDriver": "string"
                  },
                  "riskHeatmap": {
                    "domainSeverity": {"Payments": "HIGH"},
                    "totalImpacts": 0,
                    "criticalCount": 0,
                    "highCount": 0,
                    "mediumCount": 0,
                    "lowCount": 0,
                    "overallRisk": "CRITICAL|HIGH|MEDIUM|LOW"
                  }
                }

                impactItem schema:
                {
                  "componentName": "string (must match inventory name when applicable)",
                  "componentType": "APPLICATION|MICROSERVICE|API|DATABASE|BUSINESS_TEAM|CUSTOMER|TRANSACTION|TRADE_FINANCE_DEAL|PAYMENT_RECORD|PROCESS|COMPLIANCE_CONTROL",
                  "category": "APPLICATION|MICROSERVICE|API|DATABASE|BUSINESS_TEAM|CUSTOMER|TRANSACTION|OPERATIONAL|ENGINEERING|COMPLIANCE_RISK",
                  "reason": "detailed explanation of why this component is impacted",
                  "severity": "CRITICAL|HIGH|MEDIUM|LOW",
                  "confidence": 0.85,
                  "evidence": "regulatory citation or requirement text supporting the impact"
                }

                Rules:
                - Only include components genuinely impacted by the regulation.
                - Prefer components from the provided enterprise inventory when they match.
                - confidence must be between 0.0 and 1.0.
                - Populate riskHeatmap counts from the impact items you identify.
                - Be specific and actionable; do not use generic placeholder text.
                """;
    }

    static String userPrompt(RegulatoryIntelligence intelligence, List<KnowledgeBaseComponent> inventory) {
        String inventoryText = inventory.stream()
                .map(c -> "- " + c.getName() + " [" + c.getComponentType() + "/" + c.getCategory() + "]: "
                        + c.getDescription() + " | tags: " + String.join(", ", c.getTags()))
                .collect(Collectors.joining("\n"));

        return """
                Analyze enterprise impact for this regulation.

                REGULATORY INTELLIGENCE:
                %s

                ENTERPRISE COMPONENT INVENTORY:
                %s
                """.formatted(intelligenceSummary(intelligence), inventoryText);
    }

    private static String intelligenceSummary(RegulatoryIntelligence intelligence) {
        StringBuilder sb = new StringBuilder();
        sb.append("Authority: ").append(intelligence.getRegulatoryAuthority()).append('\n');
        sb.append("Document Type: ").append(intelligence.getDocumentType()).append('\n');
        sb.append("Severity: ").append(intelligence.getSeverity()).append('\n');
        sb.append("Executive Summary: ").append(intelligence.getExecutiveSummary()).append('\n');
        sb.append("Jurisdictions: ").append(String.join(", ", intelligence.getJurisdictions())).append('\n');
        sb.append("Industries: ").append(String.join(", ", intelligence.getIndustries())).append('\n');
        sb.append("Business Domains: ").append(String.join(", ", intelligence.getBusinessDomains())).append('\n');
        if (intelligence.getSanctionsInformation() != null) {
            sb.append("Sanctions: ").append(intelligence.getSanctionsInformation()).append('\n');
        }
        sb.append("New Requirements:\n");
        intelligence.getNewRequirements().forEach(r ->
                sb.append("  - [").append(r.getRequirementId()).append("] ")
                        .append(r.getTitle()).append(": ").append(r.getDescription()).append('\n'));
        sb.append("Key Changes:\n");
        intelligence.getKeyChanges().forEach(c ->
                sb.append("  - [").append(c.getChangeType()).append("] ").append(c.getSummary()).append('\n'));
        sb.append("Deadlines:\n");
        intelligence.getDeadlines().forEach(d ->
                sb.append("  - ").append(d.getDescription()).append(" by ").append(d.getDueDate()).append('\n'));
        return sb.toString();
    }
}
