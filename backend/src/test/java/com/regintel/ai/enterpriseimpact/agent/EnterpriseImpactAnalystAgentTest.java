package com.regintel.ai.enterpriseimpact.agent;

import com.regintel.ai.enterpriseimpact.knowledge.MockEnterpriseKnowledgeBase;
import com.regintel.ai.enterpriseimpact.schema.ComplianceRiskSchema;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.enterpriseimpact.schema.ImpactItemSchema;
import com.regintel.ai.enterpriseimpact.schema.RiskHeatmapSchema;
import com.regintel.ai.enterpriseimpact.entity.ComponentType;
import com.regintel.ai.enterpriseimpact.entity.ImpactCategory;
import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import com.regintel.ai.llm.service.LlmService;
import com.regintel.ai.llm.support.LlmJsonParser;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import com.regintel.ai.regulationintelligence.schema.RegulatoryRequirement;
import com.regintel.ai.regulationintelligence.schema.SourceReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnterpriseImpactAnalystAgentTest {

    @Mock
    private LlmService llmService;

    private EnterpriseImpactAnalystAgent agent;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        agent = new EnterpriseImpactAnalystAgent(
                llmService,
                new LlmJsonParser(objectMapper),
                new MockEnterpriseKnowledgeBase());
    }

    @Test
    void analyze_parsesLlmResponseIntoImpactAssessment() throws Exception {
        when(llmService.isEnabled()).thenReturn(true);
        when(llmService.hasAvailableProvider()).thenReturn(true);

        EnterpriseImpactAssessmentSchema llmResult = EnterpriseImpactAssessmentSchema.builder()
                .microserviceImpacts(List.of(
                        impact("sanctions-checker", ComponentType.MICROSERVICE, ImpactCategory.MICROSERVICE),
                        impact("payment-processor", ComponentType.MICROSERVICE, ImpactCategory.MICROSERVICE)))
                .complianceRiskImpacts(List.of(
                        impact("Sanctions Compliance Control", ComponentType.COMPLIANCE_CONTROL,
                                ImpactCategory.COMPLIANCE_RISK)))
                .complianceRisk(ComplianceRiskSchema.builder()
                        .summary("Sanctions controls impacted")
                        .overallSeverity(ImpactSeverity.CRITICAL)
                        .affectedControls(1)
                        .primaryRegulatoryDriver("US Treasury")
                        .build())
                .riskHeatmap(RiskHeatmapSchema.builder()
                        .overallRisk(ImpactSeverity.CRITICAL)
                        .totalImpacts(8)
                        .criticalCount(2)
                        .build())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        when(llmService.completeJson(anyString(), anyString()))
                .thenReturn(mapper.writeValueAsString(llmResult));

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
                .extracting(ImpactItemSchema::getComponentName)
                .contains("sanctions-checker", "payment-processor");
        assertThat(result.getComplianceRiskImpacts()).isNotEmpty();
        assertThat(result.getRiskHeatmap().getOverallRisk()).isEqualTo(ImpactSeverity.CRITICAL);
        assertThat(result.getRiskHeatmap().getTotalImpacts()).isGreaterThan(5);
    }

    private ImpactItemSchema impact(String name, ComponentType type, ImpactCategory category) {
        return ImpactItemSchema.builder()
                .componentName(name)
                .componentType(type)
                .category(category)
                .reason("Regulatory sanctions requirement")
                .severity(ImpactSeverity.CRITICAL)
                .confidence(new BigDecimal("0.9500"))
                .evidence("OFAC screening required")
                .build();
    }
}
