package com.regintel.ai.regintel.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.agentorchestration.entity.WorkflowStatus;
import com.regintel.ai.regintel.dto.RegIntelAnalysisResponse;
import com.regintel.ai.regintel.dto.RegIntelAnalyzeRequest;
import com.regintel.ai.regintel.dto.RegIntelStatusResponse;
import com.regintel.ai.regintel.service.RegIntelOrchestrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regintel")
@RequiredArgsConstructor
@Tag(name = "RegIntel Orchestration", description = "End-to-end RegIntel AI analysis pipeline")
public class RegIntelOrchestrationController {

    private final RegIntelOrchestrationService orchestrationService;

    @PostMapping("/analyze")
    @Operation(summary = "Run full RegIntel analysis pipeline",
            description = "Upload regulation → Intelligence → Impact → Delivery Plan → Executive Copilot → Final Analysis")
    public ResponseEntity<ApiResponse<RegIntelAnalysisResponse>> analyze(
            @Valid @RequestBody RegIntelAnalyzeRequest request) {
        RegIntelAnalysisResponse response = orchestrationService.analyze(request);
        if (response.getStatus() == WorkflowStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "RegIntel analysis completed successfully"));
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.success(response, "RegIntel analysis failed — use workflowId to retry"));
    }

    @PostMapping(value = "/analyze/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a regulation file and run full RegIntel analysis pipeline",
            description = "Extracts text from PDF/DOCX/TXT and runs Intelligence → Impact → Delivery → Executive Copilot")
    public ResponseEntity<ApiResponse<RegIntelAnalysisResponse>> analyzeUpload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("source") String source,
            @RequestParam("jurisdiction") String jurisdiction,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "effectiveDate", required = false) LocalDate effectiveDate) {
        RegIntelAnalysisResponse response = orchestrationService.analyzeFromUpload(
                file, title, source, jurisdiction, documentType, effectiveDate);
        if (response.getStatus() == WorkflowStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "RegIntel analysis completed successfully"));
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.success(response, "RegIntel analysis failed — use workflowId to retry"));
    }

    @GetMapping("/{workflowId}")
    @Operation(summary = "Get complete RegIntel analysis by workflow ID",
            description = "Returns aggregated Regulation Intelligence, Enterprise Impact, Engineering Plan, and Executive Report")
    public ResponseEntity<ApiResponse<RegIntelAnalysisResponse>> getAnalysis(
            @PathVariable UUID workflowId) {
        return ResponseEntity.ok(ApiResponse.success(orchestrationService.getAnalysis(workflowId)));
    }

    @GetMapping("/{workflowId}/status")
    @Operation(summary = "Get workflow execution status and step progress")
    public ResponseEntity<ApiResponse<RegIntelStatusResponse>> getStatus(
            @PathVariable UUID workflowId) {
        return ResponseEntity.ok(ApiResponse.success(orchestrationService.getStatus(workflowId)));
    }

    @GetMapping("/{workflowId}/executive-report")
    @Operation(summary = "Get executive decision report with full aggregated analysis")
    public ResponseEntity<ApiResponse<RegIntelAnalysisResponse>> getExecutiveReport(
            @PathVariable UUID workflowId) {
        return ResponseEntity.ok(ApiResponse.success(orchestrationService.getExecutiveReport(workflowId)));
    }

    @PostMapping("/{workflowId}/retry")
    @Operation(summary = "Retry a failed RegIntel workflow from the last failed step")
    public ResponseEntity<ApiResponse<RegIntelAnalysisResponse>> retry(
            @PathVariable UUID workflowId) {
        RegIntelAnalysisResponse response = orchestrationService.retry(workflowId);
        if (response.getStatus() == WorkflowStatus.COMPLETED) {
            return ResponseEntity.ok(ApiResponse.success(response, "RegIntel workflow retry completed"));
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.success(response, "RegIntel workflow retry failed"));
    }
}
