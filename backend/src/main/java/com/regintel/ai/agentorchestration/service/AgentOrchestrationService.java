package com.regintel.ai.agentorchestration.service;

import com.regintel.ai.agentorchestration.dto.AgentWorkflowDto;
import com.regintel.ai.agentorchestration.entity.*;
import com.regintel.ai.agentorchestration.repository.AgentTaskRepository;
import com.regintel.ai.agentorchestration.repository.AgentWorkflowRepository;
import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.service.RegulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentOrchestrationService {

    private final AgentWorkflowRepository workflowRepository;
    private final AgentTaskRepository taskRepository;
    private final RegulationService regulationService;

    @Transactional
    public AgentWorkflowDto.WorkflowResponse startWorkflow(AgentWorkflowDto.WorkflowRequest request) {
        log.info("Starting agent workflow: {}", request.getName());
        Regulation regulation = null;
        if (request.getRegulationId() != null) {
            regulation = regulationService.getEntity(request.getRegulationId());
        }
        AgentWorkflow workflow = AgentWorkflow.builder()
                .name(request.getName())
                .workflowType(request.getWorkflowType())
                .regulation(regulation)
                .status(request.getStatus() != null ? request.getStatus() : WorkflowStatus.PENDING)
                .startedAt(LocalDateTime.now())
                .build();
        return toWorkflowResponse(workflowRepository.save(workflow));
    }

    @Transactional(readOnly = true)
    public List<AgentWorkflowDto.WorkflowResponse> findAllWorkflows() {
        return workflowRepository.findAll().stream().map(this::toWorkflowResponse).toList();
    }

    @Transactional(readOnly = true)
    public AgentWorkflowDto.WorkflowResponse findWorkflowById(UUID id) {
        return toWorkflowResponse(getWorkflowEntity(id));
    }

    @Transactional
    public AgentWorkflowDto.TaskResponse addTask(UUID workflowId, AgentWorkflowDto.TaskRequest request) {
        log.info("Adding agent task to workflow: {}", workflowId);
        AgentWorkflow workflow = getWorkflowEntity(workflowId);
        if (workflow.getStatus() == WorkflowStatus.PENDING) {
            workflow.setStatus(WorkflowStatus.RUNNING);
            workflowRepository.save(workflow);
        }
        AgentTask task = AgentTask.builder()
                .workflow(workflow)
                .agentType(request.getAgentType())
                .taskType(request.getTaskType())
                .inputPayload(request.getInputPayload())
                .status(request.getStatus() != null ? request.getStatus() : AgentTaskStatus.PENDING)
                .build();
        return toTaskResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<AgentWorkflowDto.TaskResponse> findTasksByWorkflowId(UUID workflowId) {
        getWorkflowEntity(workflowId);
        return taskRepository.findByWorkflow_Id(workflowId).stream()
                .map(this::toTaskResponse)
                .toList();
    }

    @Transactional
    public AgentWorkflowDto.WorkflowResponse completeWorkflow(UUID id) {
        log.info("Completing agent workflow: {}", id);
        AgentWorkflow workflow = getWorkflowEntity(id);
        workflow.setStatus(WorkflowStatus.COMPLETED);
        workflow.setCompletedAt(LocalDateTime.now());
        return toWorkflowResponse(workflowRepository.save(workflow));
    }

    public AgentWorkflow getWorkflowEntity(UUID id) {
        return workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgentWorkflow", id));
    }

    private AgentWorkflowDto.WorkflowResponse toWorkflowResponse(AgentWorkflow workflow) {
        List<AgentWorkflowDto.TaskResponse> tasks = workflow.getTasks().stream()
                .map(this::toTaskResponse)
                .toList();
        return AgentWorkflowDto.WorkflowResponse.builder()
                .id(workflow.getId())
                .name(workflow.getName())
                .workflowType(workflow.getWorkflowType())
                .status(workflow.getStatus())
                .currentStep(workflow.getCurrentStep())
                .regulationId(workflow.getRegulation() != null ? workflow.getRegulation().getId() : null)
                .startedAt(workflow.getStartedAt())
                .completedAt(workflow.getCompletedAt())
                .tasks(tasks)
                .createdAt(workflow.getCreatedAt())
                .updatedAt(workflow.getUpdatedAt())
                .build();
    }

    private AgentWorkflowDto.TaskResponse toTaskResponse(AgentTask task) {
        return AgentWorkflowDto.TaskResponse.builder()
                .id(task.getId())
                .workflowId(task.getWorkflow().getId())
                .agentType(task.getAgentType())
                .taskType(task.getTaskType())
                .status(task.getStatus())
                .inputPayload(task.getInputPayload())
                .outputPayload(task.getOutputPayload())
                .errorMessage(task.getErrorMessage())
                .startedAt(task.getStartedAt())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
