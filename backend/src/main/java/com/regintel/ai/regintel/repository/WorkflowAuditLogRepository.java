package com.regintel.ai.regintel.repository;

import com.regintel.ai.regintel.entity.WorkflowAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowAuditLogRepository extends JpaRepository<WorkflowAuditLog, UUID> {

    List<WorkflowAuditLog> findByWorkflow_IdOrderByRecordedAtAsc(UUID workflowId);
}
