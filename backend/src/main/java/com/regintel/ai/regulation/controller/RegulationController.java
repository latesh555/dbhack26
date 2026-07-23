package com.regintel.ai.regulation.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.regulation.dto.RegulationDto;
import com.regintel.ai.regulation.service.RegulationService;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regulations")
@RequiredArgsConstructor
@Tag(name = "Regulation", description = "Regulatory document management APIs")
public class RegulationController {

    private final RegulationService regulationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new regulation document")
    public ResponseEntity<ApiResponse<RegulationDto.Response>> create(
            @Valid @RequestBody RegulationDto.Request request) {
        RegulationDto.Response response = regulationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Regulation created successfully"));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a regulatory document file and extract text content")
    public ResponseEntity<ApiResponse<RegulationDto.UploadResponse>> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("source") String source,
            @RequestParam("jurisdiction") String jurisdiction,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "effectiveDate", required = false) LocalDate effectiveDate) {
        RegulationDto.UploadResponse response = regulationService.createFromUpload(
                file, title, source, jurisdiction, documentType, effectiveDate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Document uploaded and text extracted successfully"));
    }

    @GetMapping
    @Operation(summary = "List all regulations")
    public ResponseEntity<ApiResponse<List<RegulationDto.Response>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(regulationService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get regulation by ID")
    public ResponseEntity<ApiResponse<RegulationDto.Response>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(regulationService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a regulation")
    public ResponseEntity<ApiResponse<RegulationDto.Response>> update(
            @PathVariable UUID id,
            @Valid @RequestBody RegulationDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success(
                regulationService.update(id, request), "Regulation updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a regulation")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        regulationService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Regulation deleted successfully"));
    }
}
