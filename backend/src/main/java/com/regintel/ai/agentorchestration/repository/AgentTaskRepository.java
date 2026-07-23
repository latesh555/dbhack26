package com.regintel.ai.agentorchestration.repository;

import com.regintel.ai.agentorchestration.entity.AgentTask;
import com.regintel.ai.agentorchestration.entity.AgentTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentTaskRepository extends JpaRepository<AgentTask, UUID> {

    List<AgentTask> findByWorkflow_Id(UUID workflowId);

    List<AgentTask> findByStatus(AgentTaskStatus status);
}
