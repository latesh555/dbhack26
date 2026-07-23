package com.regintel.ai.agentorchestration.controller;

import com.regintel.ai.agentorchestration.dto.AgentWorkflowDto;
import com.regintel.ai.agentorchestration.service.AgentOrchestrationService;
import com.regintel.ai.common.dto.ApiResponse;
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
@RequestMapping("/api/v1/workflows")
@RequiredArgsConstructor
@Tag(name = "Agent Orchestration", description = "Agent workflow orchestration APIs")
public class AgentOrchestrationController {

    private final AgentOrchestrationService orchestrationService;

    @PostMapping
    @Operation(summary = "Start a new agent workflow")
    public ResponseEntity<ApiResponse<AgentWorkflowDto.WorkflowResponse>> startWorkflow(
            @Valid @RequestBody AgentWorkflowDto.WorkflowRequest request) {
        AgentWorkflowDto.WorkflowResponse response = orchestrationService.startWorkflow(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Workflow started successfully"));
    }

    @GetMapping
    @Operation(summary = "List all agent workflows")
    public ResponseEntity<ApiResponse<List<AgentWorkflowDto.WorkflowResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(orchestrationService.findAllWorkflows()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workflow by ID")
    public ResponseEntity<ApiResponse<AgentWorkflowDto.WorkflowResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(orchestrationService.findWorkflowById(id)));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Mark workflow as completed")
    public ResponseEntity<ApiResponse<AgentWorkflowDto.WorkflowResponse>> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(
                orchestrationService.completeWorkflow(id), "Workflow completed successfully"));
    }

    @PostMapping("/{workflowId}/tasks")
    @Operation(summary = "Add task to a workflow")
    public ResponseEntity<ApiResponse<AgentWorkflowDto.TaskResponse>> addTask(
            @PathVariable UUID workflowId,
            @Valid @RequestBody AgentWorkflowDto.TaskRequest request) {
        AgentWorkflowDto.TaskResponse response = orchestrationService.addTask(workflowId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Agent task created successfully"));
    }

    @GetMapping("/{workflowId}/tasks")
    @Operation(summary = "List tasks for a workflow")
    public ResponseEntity<ApiResponse<List<AgentWorkflowDto.TaskResponse>>> findTasks(
            @PathVariable UUID workflowId) {
        return ResponseEntity.ok(ApiResponse.success(orchestrationService.findTasksByWorkflowId(workflowId)));
    }
}
