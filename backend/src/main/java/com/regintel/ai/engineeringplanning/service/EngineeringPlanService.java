package com.regintel.ai.engineeringplanning.service;

import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.engineeringplanning.dto.EngineeringPlanDto;
import com.regintel.ai.engineeringplanning.entity.*;
import com.regintel.ai.engineeringplanning.repository.EngineeringPlanRepository;
import com.regintel.ai.engineeringplanning.repository.EngineeringTaskRepository;
import com.regintel.ai.enterpriseimpact.entity.ImpactAssessment;
import com.regintel.ai.enterpriseimpact.service.ImpactAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EngineeringPlanService {

    private final EngineeringPlanRepository planRepository;
    private final EngineeringTaskRepository taskRepository;
    private final ImpactAssessmentService impactService;

    @Transactional
    public EngineeringPlanDto.PlanResponse createPlan(UUID impactId, EngineeringPlanDto.PlanRequest request) {
        log.info("Creating engineering plan for impact: {}", impactId);
        ImpactAssessment impact = impactService.getEntity(impactId);
        EngineeringPlan plan = EngineeringPlan.builder()
                .impactAssessment(impact)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .estimatedEffortDays(request.getEstimatedEffortDays())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.DRAFT)
                .build();
        return toPlanResponse(planRepository.save(plan));
    }

    @Transactional(readOnly = true)
    public List<EngineeringPlanDto.PlanResponse> findPlansByImpactId(UUID impactId) {
        impactService.getEntity(impactId);
        return planRepository.findByImpactAssessment_Id(impactId).stream()
                .map(this::toPlanResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EngineeringPlanDto.PlanResponse findPlanById(UUID id) {
        return toPlanResponse(getPlanEntity(id));
    }

    @Transactional
    public EngineeringPlanDto.TaskResponse addTask(UUID planId, EngineeringPlanDto.TaskRequest request) {
        log.info("Adding task to engineering plan: {}", planId);
        EngineeringPlan plan = getPlanEntity(planId);
        EngineeringTask task = EngineeringTask.builder()
                .engineeringPlan(plan)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .estimatedHours(request.getEstimatedHours())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                .build();
        return toTaskResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<EngineeringPlanDto.TaskResponse> findTasksByPlanId(UUID planId) {
        getPlanEntity(planId);
        return taskRepository.findByEngineeringPlan_Id(planId).stream()
                .map(this::toTaskResponse)
                .toList();
    }

    public EngineeringPlan getPlanEntity(UUID id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EngineeringPlan", id));
    }

    private EngineeringPlanDto.PlanResponse toPlanResponse(EngineeringPlan plan) {
        List<EngineeringPlanDto.TaskResponse> tasks = plan.getTasks().stream()
                .map(this::toTaskResponse)
                .toList();
        return EngineeringPlanDto.PlanResponse.builder()
                .id(plan.getId())
                .impactAssessmentId(plan.getImpactAssessment().getId())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .priority(plan.getPriority())
                .estimatedEffortDays(plan.getEstimatedEffortDays())
                .status(plan.getStatus())
                .tasks(tasks)
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    private EngineeringPlanDto.TaskResponse toTaskResponse(EngineeringTask task) {
        return EngineeringPlanDto.TaskResponse.builder()
                .id(task.getId())
                .engineeringPlanId(task.getEngineeringPlan().getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .estimatedHours(task.getEstimatedHours())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
