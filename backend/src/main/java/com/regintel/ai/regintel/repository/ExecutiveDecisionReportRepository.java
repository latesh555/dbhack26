package com.regintel.ai.regintel.repository;

import com.regintel.ai.regintel.entity.ExecutiveDecisionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExecutiveDecisionReportRepository extends JpaRepository<ExecutiveDecisionReport, UUID> {

    Optional<ExecutiveDecisionReport> findByWorkflow_Id(UUID workflowId);
}
