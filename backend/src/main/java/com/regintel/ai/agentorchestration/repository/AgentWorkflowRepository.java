package com.regintel.ai.agentorchestration.repository;

import com.regintel.ai.agentorchestration.entity.AgentWorkflow;
import com.regintel.ai.agentorchestration.entity.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentWorkflowRepository extends JpaRepository<AgentWorkflow, UUID> {

    List<AgentWorkflow> findByStatus(WorkflowStatus status);

    List<AgentWorkflow> findByRegulation_Id(UUID regulationId);
}
