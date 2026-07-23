package com.regintel.ai.enterpriseimpact.agent;

import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import com.regintel.ai.regulationintelligence.schema.RegulatoryRequirement;
import com.regintel.ai.regulationintelligence.schema.SourceReference;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EnterpriseImpactAnalystAgentTest {

    private final EnterpriseImpactAnalystAgent agent =
            new EnterpriseImpactAnalystAgent(new com.regintel.ai.enterpriseimpact.knowledge.MockEnterpriseKnowledgeBase());

    @Test
    void analyze_mapsSanctionsRequirementsToComplianceAndPaymentComponents() {
        RegulatoryIntelligence intelligence = RegulatoryIntelligence.builder()
                .documentType("RULE")
                .regulatoryAuthority("US Treasury")
                .severity("CRITICAL")
                .executiveSummary("Mandatory sanctions screening for all SWIFT cross-border payment transactions.")
                .jurisdictions(List.of("US"))
                .industries(List.of("Banking"))
                .businessDomains(List.of("Compliance", "Payments"))
                .sanctionsInformation("OFAC SDN list updates require immediate screening recalibration.")
                .newRequirements(List.of(
                        RegulatoryRequirement.builder()
                                .requirementId("REQ-10")
                                .title("Sanctions screening")
                                .description("All SWIFT payment transactions must be screened against OFAC lists.")
                                .sourceReference(SourceReference.builder()
                                        .pageNumber(3)
                                        .section("Section 2")
                                        .supportingText("Screen all SWIFT transfers.")
                                        .confidenceScore(0.99)
                                        .build())
                                .build()))
                .build();

        EnterpriseImpactAssessmentSchema result = agent.analyze(intelligence);

        assertThat(result.getMicroserviceImpacts())
                .extracting(i -> i.getComponentName())
                .contains("sanctions-checker", "payment-processor");
        assertThat(result.getComplianceRiskImpacts()).isNotEmpty();
        assertThat(result.getRiskHeatmap().getOverallRisk().name()).isEqualTo("CRITICAL");
        assertThat(result.getRiskHeatmap().getTotalImpacts()).isGreaterThan(5);
    }
}
