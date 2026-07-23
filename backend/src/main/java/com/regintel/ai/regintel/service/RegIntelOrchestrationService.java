package com.regintel.ai.regintel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regintel.ai.agentorchestration.entity.*;
import com.regintel.ai.agentorchestration.repository.AgentTaskRepository;
import com.regintel.ai.agentorchestration.repository.AgentWorkflowRepository;
import com.regintel.ai.common.exception.BusinessException;
import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.enterpriseimpact.schema.EnterpriseImpactAssessmentSchema;
import com.regintel.ai.enterpriseimpact.service.EnterpriseImpactAnalystService;
import com.regintel.ai.engineeringplanning.schema.EngineeringDeliveryPlanSchema;
import com.regintel.ai.engineeringplanning.service.EngineeringDeliveryPlannerService;
import com.regintel.ai.regintel.agent.ExecutiveDecisionCopilotAgent;
import com.regintel.ai.regintel.dto.RegIntelAnalysisResponse;
import com.regintel.ai.regintel.dto.RegIntelAnalyzeRequest;
import com.regintel.ai.regintel.dto.RegIntelStatusResponse;
import com.regintel.ai.regintel.entity.AuditEventType;
import com.regintel.ai.regintel.entity.ExecutiveDecisionReport;
import com.regintel.ai.regintel.entity.WorkflowAuditLog;
import com.regintel.ai.regintel.entity.WorkflowStep;
import com.regintel.ai.regintel.repository.ExecutiveDecisionReportRepository;
import com.regintel.ai.regintel.repository.WorkflowAuditLogRepository;
import com.regintel.ai.regintel.schema.ExecutiveDecisionReportSchema;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.entity.RegulationStatus;
import com.regintel.ai.regulation.repository.RegulationRepository;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import com.regintel.ai.regulationintelligence.schema.RegulatoryIntelligence;
import com.regintel.ai.regulationintelligence.service.RegulationIntelligenceAgentService;
import com.regintel.ai.regulationintelligence.service.RegulatoryIntelligenceReader;
import com.regintel.ai.regulation.service.DocumentTextExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegIntelOrchestrationService {

    private static final String WORKFLOW_TYPE = "FULL_REGINT_ANALYSIS";

    private final RegulationRepository regulationRepository;
    private final AgentWorkflowRepository workflowRepository;
    private final AgentTaskRepository taskRepository;
    private final WorkflowAuditLogRepository auditLogRepository;
    private final ExecutiveDecisionReportRepository executiveReportRepository;
    private final RegulationIntelligenceAgentService intelligenceAgentService;
    private final RegulatoryIntelligenceReader intelligenceReader;
    private final EnterpriseImpactAnalystService impactAnalystService;
    private final EngineeringDeliveryPlannerService deliveryPlannerService;
    private final ExecutiveDecisionCopilotAgent executiveCopilotAgent;
    private final ObjectMapper objectMapper;
    private final DocumentTextExtractionService documentTextExtractionService;

    @Transactional(noRollbackFor = BusinessException.class)
    public RegIntelAnalysisResponse analyze(RegIntelAnalyzeRequest request) {
        AgentWorkflow workflow = createWorkflow(null);
        audit(workflow, null, AuditEventType.WORKFLOW_STARTED, "RegIntel end-to-end analysis started", null);

        Regulation regulation = regulationRepository.save(Regulation.builder()
                .title(request.getTitle())
                .source(request.getSource())
                .jurisdiction(request.getJurisdiction())
                .documentType(request.getDocumentType())
                .rawContent(request.getRawContent())
                .effectiveDate(request.getEffectiveDate())
                .status(RegulationStatus.ACTIVE)
                .build());

        workflow.setRegulation(regulation);
        workflowRepository.save(workflow);

        return executePipeline(workflow, regulation);
    }

    @Transactional(noRollbackFor = BusinessException.class)
    public RegIntelAnalysisResponse analyzeFromUpload(
            MultipartFile file,
            String title,
            String source,
            String jurisdiction,
            String documentType,
            LocalDate effectiveDate) {
        String rawContent = documentTextExtractionService.extractText(file);

        RegIntelAnalyzeRequest request = new RegIntelAnalyzeRequest();
        request.setTitle(title);
        request.setSource(source);
        request.setJurisdiction(jurisdiction);
        request.setDocumentType(documentType);
        request.setRawContent(rawContent);
        request.setEffectiveDate(effectiveDate);

        return analyze(request);
    }

    @Transactional(noRollbackFor = BusinessException.class)
    public RegIntelAnalysisResponse retry(UUID workflowId) {
        AgentWorkflow workflow = getWorkflow(workflowId);
        if (workflow.getStatus() != WorkflowStatus.FAILED) {
            throw new BusinessException("Workflow is not in FAILED status. Current status: " + workflow.getStatus());
        }
        Regulation regulation = workflow.getRegulation();
        if (regulation == null) {
            throw new BusinessException("Workflow has no associated regulation");
        }

        workflow.setStatus(WorkflowStatus.RUNNING);
        workflow.setErrorMessage(null);
        workflow.setRetryCount(workflow.getRetryCount() + 1);
        workflowRepository.save(workflow);

        audit(workflow, parseFailedStep(workflow.getFailedStep()), AuditEventType.STEP_RETRY,
                "Retrying workflow from step: " + workflow.getFailedStep(), null);

        return executePipeline(workflow, regulation);
    }

    @Transactional(readOnly = true)
    public RegIntelAnalysisResponse getAnalysis(UUID workflowId) {
        AgentWorkflow workflow = getWorkflow(workflowId);
        return buildResponse(workflow, loadArtifacts(workflow));
    }

    @Transactional(readOnly = true)
    public RegIntelStatusResponse getStatus(UUID workflowId) {
        AgentWorkflow workflow = getWorkflow(workflowId);
        return RegIntelStatusResponse.builder()
                .workflowId(workflow.getId())
                .regulationId(workflow.getRegulation() != null ? workflow.getRegulation().getId() : null)
                .status(workflow.getStatus())
                .currentStep(parseStep(workflow.getCurrentStep()))
                .failedStep(workflow.getFailedStep())
                .retryCount(workflow.getRetryCount() != null ? workflow.getRetryCount() : 0)
                .startedAt(workflow.getStartedAt())
                .completedAt(workflow.getCompletedAt())
                .errorMessage(workflow.getErrorMessage())
                .steps(buildStepStatuses(workflow))
                .build();
    }

    @Transactional(readOnly = true)
    public RegIntelAnalysisResponse getExecutiveReport(UUID workflowId) {
        AgentWorkflow workflow = getWorkflow(workflowId);
        if (workflow.getStatus() != WorkflowStatus.COMPLETED) {
            throw new BusinessException("Workflow not completed. Current status: " + workflow.getStatus());
        }
        return buildResponse(workflow, loadArtifacts(workflow));
    }

    private RegIntelAnalysisResponse executePipeline(AgentWorkflow workflow, Regulation regulation) {
        PipelineContext ctx = loadExistingContext(workflow, regulation);

        List<WorkflowStep> stepsToRun = determineStepsToRun(workflow, ctx);

        for (WorkflowStep step : stepsToRun) {
            workflow.setCurrentStep(step.name());
            workflowRepository.save(workflow);

            try {
                runStep(workflow, regulation, ctx, step);
            } catch (Exception ex) {
                log.error("Workflow {} failed at step {}", workflow.getId(), step, ex);
                workflow.setStatus(WorkflowStatus.FAILED);
                workflow.setFailedStep(step.name());
                workflow.setErrorMessage(ex.getMessage());
                workflowRepository.save(workflow);
                audit(workflow, step, AuditEventType.WORKFLOW_FAILED,
                        "Workflow failed at " + step + ": " + ex.getMessage(), null);
                return buildResponse(workflow, ctx);
            }
        }

        workflow.setStatus(WorkflowStatus.COMPLETED);
        workflow.setCurrentStep(WorkflowStep.FINALIZE.name());
        workflow.setCompletedAt(LocalDateTime.now());
        workflow.setFailedStep(null);
        workflow.setErrorMessage(null);
        workflowRepository.save(workflow);
        audit(workflow, WorkflowStep.FINALIZE, AuditEventType.WORKFLOW_COMPLETED,
                "RegIntel end-to-end analysis completed successfully", null);

        return buildResponse(workflow, ctx);
    }

    private void runStep(
            AgentWorkflow workflow,
            Regulation regulation,
            PipelineContext ctx,
            WorkflowStep step) throws JsonProcessingException {

        audit(workflow, step, AuditEventType.STEP_STARTED, "Starting step: " + step, null);
        AgentTask task = createTask(workflow, step);
        task.setStartedAt(LocalDateTime.now());
        task.setStatus(AgentTaskStatus.RUNNING);

        try {
            switch (step) {
                case REGULATION_UPLOAD -> {
                    task.setInputPayload(objectMapper.writeValueAsString(Map.of(
                            "regulationId", regulation.getId(),
                            "title", regulation.getTitle())));
                    ctx.regulationId = regulation.getId();
                }
                case REGULATION_INTELLIGENCE -> {
                    RegulationAnalysis analysis = intelligenceAgentService.analyzeAndPersist(regulation);
                    ctx.analysisId = analysis.getId();
                    ctx.intelligence = intelligenceReader.parseIntelligencePayload(analysis);
                    task.setOutputPayload(objectMapper.writeValueAsString(ctx.intelligence));
                }
                case ENTERPRISE_IMPACT -> {
                    ctx.impact = impactAnalystService.startAnalysis(regulation.getId());
                    task.setOutputPayload(objectMapper.writeValueAsString(ctx.impact));
                }
                case ENGINEERING_DELIVERY -> {
                    ctx.plan = deliveryPlannerService.generatePlan(regulation.getId());
                    task.setOutputPayload(objectMapper.writeValueAsString(ctx.plan));
                }
                case EXECUTIVE_COPILOT -> {
                    ctx.executiveReport = executiveCopilotAgent.generate(
                            ctx.intelligence, ctx.impact, ctx.plan);
                    persistExecutiveReport(workflow, regulation, ctx.executiveReport);
                    task.setOutputPayload(objectMapper.writeValueAsString(ctx.executiveReport));
                }
                default -> throw new BusinessException("Unknown step: " + step);
            }

            task.setStatus(AgentTaskStatus.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
            taskRepository.save(task);
            audit(workflow, step, AuditEventType.STEP_COMPLETED, "Completed step: " + step, null);

        } catch (Exception ex) {
            task.setStatus(AgentTaskStatus.FAILED);
            task.setErrorMessage(ex.getMessage());
            task.setCompletedAt(LocalDateTime.now());
            taskRepository.save(task);
            audit(workflow, step, AuditEventType.STEP_FAILED, "Step failed: " + step, ex.getMessage());
            throw ex;
        }
    }

    private List<WorkflowStep> determineStepsToRun(AgentWorkflow workflow, PipelineContext ctx) {
        if (workflow.getFailedStep() != null && workflow.getStatus() == WorkflowStatus.RUNNING) {
            WorkflowStep resumeFrom = parseFailedStep(workflow.getFailedStep());
            return Arrays.stream(WorkflowStep.values())
                    .filter(s -> s != WorkflowStep.FINALIZE)
                    .filter(s -> s.ordinal() >= resumeFrom.ordinal())
                    .toList();
        }
        if (ctx.intelligence != null && ctx.impact != null && ctx.plan != null && ctx.executiveReport != null) {
            return List.of();
        }
        return Arrays.stream(WorkflowStep.values())
                .filter(s -> s != WorkflowStep.FINALIZE)
                .filter(s -> needsStep(s, ctx))
                .toList();
    }

    private boolean needsStep(WorkflowStep step, PipelineContext ctx) {
        return switch (step) {
            case REGULATION_UPLOAD -> ctx.regulationId == null;
            case REGULATION_INTELLIGENCE -> ctx.intelligence == null;
            case ENTERPRISE_IMPACT -> ctx.impact == null;
            case ENGINEERING_DELIVERY -> ctx.plan == null;
            case EXECUTIVE_COPILOT -> ctx.executiveReport == null;
            default -> false;
        };
    }

    private PipelineContext loadExistingContext(AgentWorkflow workflow, Regulation regulation) {
        PipelineContext ctx = new PipelineContext();
        ctx.regulationId = regulation.getId();

        try {
            ctx.intelligence = intelligenceReader.readStructuredIntelligence(regulation.getId());
        } catch (Exception ignored) {
            // not yet available
        }
        try {
            ctx.impact = impactAnalystService.getByRegulationId(regulation.getId());
        } catch (Exception ignored) {
            // not yet available
        }
        try {
            ctx.plan = deliveryPlannerService.getPlanByRegulationId(regulation.getId());
        } catch (Exception ignored) {
            // not yet available
        }
        executiveReportRepository.findByWorkflow_Id(workflow.getId()).ifPresent(report -> {
            try {
                ctx.executiveReport = objectMapper.readValue(
                        report.getReportPayload(), ExecutiveDecisionReportSchema.class);
            } catch (JsonProcessingException ignored) {
                // ignore
            }
        });

        return ctx;
    }

    private PipelineContext loadArtifacts(AgentWorkflow workflow) {
        Regulation regulation = workflow.getRegulation();
        if (regulation == null) {
            throw new BusinessException("Workflow has no associated regulation");
        }
        return loadExistingContext(workflow, regulation);
    }

    private void persistExecutiveReport(
            AgentWorkflow workflow,
            Regulation regulation,
            ExecutiveDecisionReportSchema report) throws JsonProcessingException {

        executiveReportRepository.findByWorkflow_Id(workflow.getId()).ifPresent(executiveReportRepository::delete);

        executiveReportRepository.save(ExecutiveDecisionReport.builder()
                .workflow(workflow)
                .regulation(regulation)
                .reportPayload(objectMapper.writeValueAsString(report))
                .overallRiskScore(report.getOverallRiskScore())
                .generatedAt(LocalDateTime.now())
                .build());
    }

    private AgentWorkflow createWorkflow(Regulation regulation) {
        return workflowRepository.save(AgentWorkflow.builder()
                .name("RegIntel Full Analysis")
                .workflowType(WORKFLOW_TYPE)
                .regulation(regulation)
                .status(WorkflowStatus.RUNNING)
                .currentStep(WorkflowStep.REGULATION_UPLOAD.name())
                .startedAt(LocalDateTime.now())
                .retryCount(0)
                .build());
    }

    private AgentTask createTask(AgentWorkflow workflow, WorkflowStep step) {
        return taskRepository.save(AgentTask.builder()
                .workflow(workflow)
                .agentType(mapAgentType(step))
                .taskType(step.name())
                .status(AgentTaskStatus.PENDING)
                .build());
    }

    private AgentType mapAgentType(WorkflowStep step) {
        return switch (step) {
            case REGULATION_INTELLIGENCE -> AgentType.REGULATION_PARSER;
            case ENTERPRISE_IMPACT -> AgentType.IMPACT_ANALYZER;
            case ENGINEERING_DELIVERY -> AgentType.PLAN_GENERATOR;
            case EXECUTIVE_COPILOT -> AgentType.EXECUTIVE_COPILOT;
            default -> AgentType.REPORT_BUILDER;
        };
    }

    private void audit(
            AgentWorkflow workflow,
            WorkflowStep step,
            AuditEventType eventType,
            String message,
            String details) {
        auditLogRepository.save(WorkflowAuditLog.builder()
                .workflow(workflow)
                .stepName(step)
                .eventType(eventType)
                .message(message)
                .details(details)
                .recordedAt(LocalDateTime.now())
                .build());
    }

    private RegIntelAnalysisResponse buildResponse(AgentWorkflow workflow, PipelineContext ctx) {
        return RegIntelAnalysisResponse.builder()
                .workflowId(workflow.getId())
                .regulationId(ctx.regulationId)
                .status(workflow.getStatus())
                .currentStep(parseStep(workflow.getCurrentStep()))
                .startedAt(workflow.getStartedAt())
                .completedAt(workflow.getCompletedAt())
                .regulationIntelligence(ctx.intelligence)
                .enterpriseImpact(ctx.impact)
                .engineeringPlan(ctx.plan)
                .executiveReport(ctx.executiveReport)
                .steps(buildStepStatuses(workflow))
                .build();
    }

    private List<RegIntelAnalysisResponse.StepStatusDto> buildStepStatuses(AgentWorkflow workflow) {
        Map<String, AgentTask> tasksByType = new LinkedHashMap<>();
        for (AgentTask task : taskRepository.findByWorkflow_Id(workflow.getId())) {
            tasksByType.put(task.getTaskType(), task);
        }

        List<RegIntelAnalysisResponse.StepStatusDto> steps = new ArrayList<>();
        for (WorkflowStep step : WorkflowStep.values()) {
            if (step == WorkflowStep.FINALIZE) {
                continue;
            }
            AgentTask task = tasksByType.get(step.name());
            if (task != null) {
                steps.add(RegIntelAnalysisResponse.StepStatusDto.builder()
                        .step(step)
                        .status(task.getStatus().name())
                        .startedAt(task.getStartedAt())
                        .completedAt(task.getCompletedAt())
                        .errorMessage(task.getErrorMessage())
                        .retryCount(workflow.getRetryCount() != null ? workflow.getRetryCount() : 0)
                        .build());
            }
        }
        return steps;
    }

    private AgentWorkflow getWorkflow(UUID workflowId) {
        return workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("RegIntel workflow", workflowId));
    }

    private WorkflowStep parseStep(String step) {
        if (step == null) {
            return null;
        }
        try {
            return WorkflowStep.valueOf(step);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private WorkflowStep parseFailedStep(String step) {
        WorkflowStep parsed = parseStep(step);
        return parsed != null ? parsed : WorkflowStep.REGULATION_UPLOAD;
    }

    private static class PipelineContext {
        UUID regulationId;
        UUID analysisId;
        RegulatoryIntelligence intelligence;
        EnterpriseImpactAssessmentSchema impact;
        EngineeringDeliveryPlanSchema plan;
        ExecutiveDecisionReportSchema executiveReport;
    }
}
