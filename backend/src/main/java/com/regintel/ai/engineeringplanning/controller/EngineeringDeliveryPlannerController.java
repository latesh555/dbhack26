package com.regintel.ai.engineeringplanning.controller;

import com.regintel.ai.common.dto.ApiResponse;
import com.regintel.ai.engineeringplanning.dto.UpdateStoryStatusRequest;
import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import com.regintel.ai.engineeringplanning.schema.EpicSchema;
import com.regintel.ai.engineeringplanning.schema.UserStorySchema;
import com.regintel.ai.engineeringplanning.service.EngineeringDeliveryPlannerService;
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
@RequestMapping("/api/v1/regulations/{regulationId}/delivery-plan")
@RequiredArgsConstructor
@Tag(name = "Engineering Delivery Planner", description = "Agent-driven engineering delivery plan APIs")
public class EngineeringDeliveryPlannerController {

    private final EngineeringDeliveryPlannerService plannerService;

    @PostMapping("/generate")
    @Operation(summary = "Generate engineering delivery plan from enterprise impact assessment")
    public ResponseEntity<ApiResponse<EngineeringDeliveryPlanSchema>> generatePlan(
            @PathVariable UUID regulationId) {
        EngineeringDeliveryPlanSchema plan = plannerService.generatePlan(regulationId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(plan, "Engineering delivery plan generated successfully"));
    }

    @GetMapping
    @Operation(summary = "Get engineering delivery plan by regulation ID")
    public ResponseEntity<ApiResponse<EngineeringDeliveryPlanSchema>> getPlan(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getPlanByRegulationId(regulationId)));
    }

    @GetMapping("/epics")
    @Operation(summary = "Get epics for the delivery plan")
    public ResponseEntity<ApiResponse<List<EpicSchema>>> getEpics(@PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getEpics(regulationId)));
    }

    @GetMapping("/user-stories")
    @Operation(summary = "Get all user stories for the delivery plan")
    public ResponseEntity<ApiResponse<List<UserStorySchema>>> getUserStories(
            @PathVariable UUID regulationId) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getUserStories(regulationId)));
    }

    @PatchMapping("/user-stories/{storyId}/status")
    @Operation(summary = "Update user story status")
    public ResponseEntity<ApiResponse<UserStorySchema>> updateStoryStatus(
            @PathVariable UUID regulationId,
            @PathVariable UUID storyId,
            @Valid @RequestBody UpdateStoryStatusRequest request) {
        UserStorySchema updated = plannerService.updateStoryStatus(
                regulationId, storyId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(updated, "User story status updated successfully"));
    }
}
