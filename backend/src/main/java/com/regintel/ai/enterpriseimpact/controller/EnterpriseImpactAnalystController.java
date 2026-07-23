package com.regintel.ai.enterpriseimpact.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.enterpriseimpact.schema.ImpactItemSchema;
import com.regintel.ai.enterpriseimpact.schema.RiskHeatmapSchema;
import com.regintel.ai.enterpriseimpact.service.EnterpriseImpactAnalystService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulations/{regulationId}/enterprise-impact")
@RequiredArgsConstructor
@Tag(name = "Enterprise Impact Analyst", description = "Agent-driven enterprise impact analysis APIs")
public class EnterpriseImpactAnalystController {

    private final EnterpriseImpactAnalystService analystService;

    @PostMapping("/analyze")
    @Operation(summary = "Start enterprise impact analysis from regulatory intelligence")
    public ResponseEntity<ApiResponse<EnterpriseImpactAssessmentSchema>> startAnalysis(
            @PathVariable UUID regulationId) {
        EnterpriseImpactAssessmentSchema result = analystService.startAnalysis(regulationId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result, "Enterprise impact analysis completed successfully"));
    }

    @GetMapping
    @Operation(summary = "Get latest enterprise impact assessment by regulation ID")
    public ResponseEntity<ApiResponse<EnterpriseImpactAssessmentSchema>> getAssessment(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(analystService.getByRegulationId(regulationId)));
    }

    @GetMapping("/applications")
    @Operation(summary = "Get impacted applications")
    public ResponseEntity<ApiResponse<List<ImpactItemSchema>>> getImpactedApplications(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(analystService.getImpactedApplications(regulationId)));
    }

    @GetMapping("/customers")
    @Operation(summary = "Get impacted customers")
    public ResponseEntity<ApiResponse<List<ImpactItemSchema>>> getImpactedCustomers(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(analystService.getImpactedCustomers(regulationId)));
    }

    @GetMapping("/transactions")
    @Operation(summary = "Get impacted transactions and trade finance flows")
    public ResponseEntity<ApiResponse<List<ImpactItemSchema>>> getImpactedTransactions(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(analystService.getImpactedTransactions(regulationId)));
    }

    @GetMapping("/risk-heatmap")
    @Operation(summary = "Get enterprise risk heatmap")
    public ResponseEntity<ApiResponse<RiskHeatmapSchema>> getRiskHeatmap(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(analystService.getRiskHeatmap(regulationId)));
    }
}
