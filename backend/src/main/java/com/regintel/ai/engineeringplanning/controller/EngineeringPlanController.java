package com.regintel.ai.engineeringplanning.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.engineeringplanning.dto.EngineeringPlanDto;
import com.regintel.ai.engineeringplanning.service.EngineeringPlanService;
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
@Tag(name = "Engineering Planning", description = "Engineering plan and task management APIs")
public class EngineeringPlanController {

    private final EngineeringPlanService planService;

    @PostMapping("/api/v1/impacts/{impactId}/plans")
    @Operation(summary = "Create engineering plan for an impact assessment")
    public ResponseEntity<ApiResponse<EngineeringPlanDto.PlanResponse>> createPlan(
            @PathVariable UUID impactId,
            @Valid @RequestBody EngineeringPlanDto.PlanRequest request) {
        EngineeringPlanDto.PlanResponse response = planService.createPlan(impactId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Engineering plan created successfully"));
    }

    @GetMapping("/api/v1/impacts/{impactId}/plans")
    @Operation(summary = "List engineering plans for an impact assessment")
    public ResponseEntity<ApiResponse<List<EngineeringPlanDto.PlanResponse>>> findPlansByImpact(
            @PathVariable UUID impactId) {
        return ResponseEntity.ok(ApiResponse.success(planService.findPlansByImpactId(impactId)));
    }

    @GetMapping("/api/v1/plans/{id}")
    @Operation(summary = "Get engineering plan by ID")
    public ResponseEntity<ApiResponse<EngineeringPlanDto.PlanResponse>> findPlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(planService.findPlanById(id)));
    }

    @PostMapping("/api/v1/plans/{planId}/tasks")
    @Operation(summary = "Add task to an engineering plan")
    public ResponseEntity<ApiResponse<EngineeringPlanDto.TaskResponse>> addTask(
            @PathVariable UUID planId,
            @Valid @RequestBody EngineeringPlanDto.TaskRequest request) {
        EngineeringPlanDto.TaskResponse response = planService.addTask(planId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Engineering task created successfully"));
    }

    @GetMapping("/api/v1/plans/{planId}/tasks")
    @Operation(summary = "List tasks for an engineering plan")
    public ResponseEntity<ApiResponse<List<EngineeringPlanDto.TaskResponse>>> findTasksByPlan(
            @PathVariable UUID planId) {
        return ResponseEntity.ok(ApiResponse.success(planService.findTasksByPlanId(planId)));
    }
}
