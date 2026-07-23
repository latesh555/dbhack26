package com.regintel.ai.regulationintelligence.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.regulationintelligence.dto.RegulationAnalysisDto;
import com.regintel.ai.regulationintelligence.service.RegulationAnalysisService;
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
@Tag(name = "Regulation Intelligence", description = "Regulatory document analysis APIs")
public class RegulationAnalysisController {

    private final RegulationAnalysisService analysisService;

    @PostMapping("/api/v1/regulations/{regulationId}/analyses")
    @Operation(summary = "Create analysis for a regulation")
    public ResponseEntity<ApiResponse<RegulationAnalysisDto.Response>> create(
            @PathVariable UUID regulationId,
            @Valid @RequestBody RegulationAnalysisDto.Request request) {
        RegulationAnalysisDto.Response response = analysisService.create(regulationId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Analysis created successfully"));
    }

    @GetMapping("/api/v1/regulations/{regulationId}/analyses")
    @Operation(summary = "List analyses for a regulation")
    public ResponseEntity<ApiResponse<List<RegulationAnalysisDto.Response>>> findByRegulation(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(analysisService.findByRegulationId(regulationId)));
    }

    @GetMapping("/api/v1/analyses/{id}")
    @Operation(summary = "Get analysis by ID")
    public ResponseEntity<ApiResponse<RegulationAnalysisDto.Response>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(analysisService.findById(id)));
    }

    @PutMapping("/api/v1/analyses/{id}")
    @Operation(summary = "Update an analysis")
    public ResponseEntity<ApiResponse<RegulationAnalysisDto.Response>> update(
            @PathVariable UUID id,
            @Valid @RequestBody RegulationAnalysisDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success(
                analysisService.update(id, request), "Analysis updated successfully"));
    }
}
