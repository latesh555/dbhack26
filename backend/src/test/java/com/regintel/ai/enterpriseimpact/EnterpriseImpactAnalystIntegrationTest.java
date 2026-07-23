package com.regintel.ai.enterpriseimpact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.entity.RegulationStatus;
import com.regintel.ai.regulation.repository.RegulationRepository;
import com.regintel.ai.regulationintelligence.entity.AnalysisStatus;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import com.regintel.ai.regulationintelligence.entity.RiskLevel;
import com.regintel.ai.regulationintelligence.repository.RegulationAnalysisRepository;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import com.regintel.ai.regulationintelligence.schema.RegulatoryRequirement;
import com.regintel.ai.regulationintelligence.schema.SourceReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EnterpriseImpactAnalystIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegulationRepository regulationRepository;

    @Autowired
    private RegulationAnalysisRepository analysisRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID regulationId;

    @BeforeEach
    void setUp() throws Exception {
        Regulation regulation = regulationRepository.save(Regulation.builder()
                .title("EU Sanctions Amendment 2026")
                .source("European Commission")
                .jurisdiction("EU")
                .documentType("DIRECTIVE")
                .status(RegulationStatus.ACTIVE)
                .effectiveDate(LocalDate.of(2026, 9, 1))
                .build());

        RegulatoryIntelligence intelligence = RegulatoryIntelligence.builder()
                .documentType("DIRECTIVE")
                .regulatoryAuthority("European Commission")
                .publicationDate(LocalDate.of(2026, 3, 1))
                .effectiveDate(LocalDate.of(2026, 9, 1))
                .severity("HIGH")
                .executiveSummary("New sanctions screening and payment reporting obligations for EU banks.")
                .jurisdictions(List.of("EU"))
                .industries(List.of("Banking", "Financial Services"))
                .businessDomains(List.of("Compliance", "Payments", "Trade Finance"))
                .sanctionsInformation("Expanded OFAC-aligned sanctions list screening required for cross-border payments.")
                .newRequirements(List.of(
                        RegulatoryRequirement.builder()
                                .requirementId("REQ-001")
                                .title("Enhanced sanctions screening")
                                .description("All cross-border payment and SWIFT transactions must pass real-time sanctions screening.")
                                .sourceReference(SourceReference.builder()
                                        .pageNumber(12)
                                        .section("Article 4.2")
                                        .supportingText("Payment institutions shall screen all cross-border transfers against updated sanctions lists.")
                                        .confidenceScore(0.96)
                                        .build())
                                .build(),
                        RegulatoryRequirement.builder()
                                .requirementId("REQ-002")
                                .title("Trade finance LC documentary checks")
                                .description("Import letter of credit workflows must include enhanced documentary compliance checks.")
                                .sourceReference(SourceReference.builder()
                                        .pageNumber(18)
                                        .section("Article 7.1")
                                        .supportingText("Documentary trade finance products require updated compliance attestation.")
                                        .confidenceScore(0.91)
                                        .build())
                                .build()))
                .build();

        analysisRepository.save(RegulationAnalysis.builder()
                .regulation(regulation)
                .summary(intelligence.getExecutiveSummary())
                .keyRequirements("Sanctions screening; trade finance compliance")
                .complianceAreas("Sanctions, Payments, Trade Finance")
                .riskLevel(RiskLevel.HIGH)
                .status(AnalysisStatus.COMPLETED)
                .analyzedAt(LocalDateTime.now())
                .intelligencePayload(objectMapper.writeValueAsString(intelligence))
                .build());

        regulationId = regulation.getId();
    }

    @Test
    void startAnalysis_persistsCompleteAssessment() throws Exception {
        mockMvc.perform(post("/api/v1/regulations/{id}/enterprise-impact/analyze", regulationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.regulationId").value(regulationId.toString()))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.applicationImpacts", not(empty())))
                .andExpect(jsonPath("$.data.microserviceImpacts", not(empty())))
                .andExpect(jsonPath("$.data.complianceRisk.overallSeverity").value("HIGH"))
                .andExpect(jsonPath("$.data.riskHeatmap.totalImpacts", greaterThan(0)));
    }

    @Test
    void getAssessment_returnsPersistedResult() throws Exception {
        mockMvc.perform(post("/api/v1/regulations/{id}/enterprise-impact/analyze", regulationId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/regulations/{id}/enterprise-impact", regulationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.regulationId").value(regulationId.toString()))
                .andExpect(jsonPath("$.data.databaseImpacts").isArray())
                .andExpect(jsonPath("$.data.engineeringImpacts").isArray());
    }

    @Test
    void getImpactedApplications_returnsApplicationCategoryOnly() throws Exception {
        mockMvc.perform(post("/api/v1/regulations/{id}/enterprise-impact/analyze", regulationId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/regulations/{id}/enterprise-impact/applications", regulationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", not(empty())))
                .andExpect(jsonPath("$.data[0].componentType").value("APPLICATION"))
                .andExpect(jsonPath("$.data[0].componentName").exists())
                .andExpect(jsonPath("$.data[0].severity").exists())
                .andExpect(jsonPath("$.data[0].confidence").exists())
                .andExpect(jsonPath("$.data[0].evidence").exists());
    }

    @Test
    void getImpactedCustomers_returnsCustomerImpacts() throws Exception {
        mockMvc.perform(post("/api/v1/regulations/{id}/enterprise-impact/analyze", regulationId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/regulations/{id}/enterprise-impact/customers", regulationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].componentType").value("CUSTOMER"));
    }

    @Test
    void getImpactedTransactions_returnsTransactionImpacts() throws Exception {
        mockMvc.perform(post("/api/v1/regulations/{id}/enterprise-impact/analyze", regulationId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/regulations/{id}/enterprise-impact/transactions", regulationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", not(empty())))
                .andExpect(jsonPath("$.data[*].componentType",
                        everyItem(anyOf(is("TRANSACTION"), is("TRADE_FINANCE_DEAL"), is("PAYMENT_RECORD")))));
    }

    @Test
    void getRiskHeatmap_returnsAggregatedRisk() throws Exception {
        mockMvc.perform(post("/api/v1/regulations/{id}/enterprise-impact/analyze", regulationId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/regulations/{id}/enterprise-impact/risk-heatmap", regulationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.overallRisk").exists())
                .andExpect(jsonPath("$.data.totalImpacts", greaterThan(0)))
                .andExpect(jsonPath("$.data.domainSeverity").isMap());
    }

    @Test
    void startAnalysis_failsWithoutIntelligencePayload() throws Exception {
        Regulation regulation = regulationRepository.save(Regulation.builder()
                .title("Incomplete Regulation")
                .source("Test")
                .jurisdiction("US")
                .documentType("RULE")
                .status(RegulationStatus.DRAFT)
                .build());

        analysisRepository.save(RegulationAnalysis.builder()
                .regulation(regulation)
                .summary("No structured payload")
                .riskLevel(RiskLevel.LOW)
                .status(AnalysisStatus.COMPLETED)
                .analyzedAt(LocalDateTime.now())
                .build());

        mockMvc.perform(post("/api/v1/regulations/{id}/enterprise-impact/analyze", regulation.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("structured intelligence")));
    }
}
