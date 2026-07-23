package com.regintel.ai.executivereporting.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.executivereporting.dto.ExecutiveReportDto;
import com.regintel.ai.executivereporting.service.ExecutiveReportService;
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
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Executive Reporting", description = "Executive report generation APIs")
public class ExecutiveReportController {

    private final ExecutiveReportService reportService;

    @PostMapping
    @Operation(summary = "Create an executive report")
    public ResponseEntity<ApiResponse<ExecutiveReportDto.Response>> create(
            @Valid @RequestBody ExecutiveReportDto.Request request) {
        ExecutiveReportDto.Response response = reportService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Executive report created successfully"));
    }

    @GetMapping
    @Operation(summary = "List all executive reports")
    public ResponseEntity<ApiResponse<List<ExecutiveReportDto.Response>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(reportService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get executive report by ID")
    public ResponseEntity<ApiResponse<ExecutiveReportDto.Response>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(reportService.findById(id)));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish an executive report")
    public ResponseEntity<ApiResponse<ExecutiveReportDto.Response>> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(
                reportService.publish(id), "Executive report published successfully"));
    }
}
