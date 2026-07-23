package com.regintel.ai.enterpriseimpact.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.enterpriseimpact.dto.ImpactAssessmentDto;
import com.regintel.ai.enterpriseimpact.service.ImpactAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Enterprise Impact", description = "Enterprise impact assessment APIs")
public class ImpactAssessmentController {

    private final ImpactAssessmentService impactService;

    @PostMapping("/api/v1/analyses/{analysisId}/impacts")
    @Operation(summary = "Create impact assessment for an analysis")
    public ResponseEntity<ApiResponse<ImpactAssessmentDto.Response>> create(
            @PathVariable UUID analysisId,
            @Valid @RequestBody ImpactAssessmentDto.Request request) {
        ImpactAssessmentDto.Response response = impactService.create(analysisId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Impact assessment created successfully"));
    }

    @GetMapping("/api/v1/analyses/{analysisId}/impacts")
    @Operation(summary = "List impact assessments for an analysis")
    public ResponseEntity<ApiResponse<List<ImpactAssessmentDto.Response>>> findByAnalysis(
            @PathVariable UUID analysisId) {
        return ResponseEntity.ok(ApiResponse.success(impactService.findByAnalysisId(analysisId)));
    }

    @GetMapping("/api/v1/impacts/{id}")
    @Operation(summary = "Get impact assessment by ID")
    public ResponseEntity<ApiResponse<ImpactAssessmentDto.Response>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(impactService.findById(id)));
    }

    @PutMapping("/api/v1/impacts/{id}")
    @Operation(summary = "Update an impact assessment")
    public ResponseEntity<ApiResponse<ImpactAssessmentDto.Response>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ImpactAssessmentDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success(
                impactService.update(id, request), "Impact assessment updated successfully"));
    }
}
