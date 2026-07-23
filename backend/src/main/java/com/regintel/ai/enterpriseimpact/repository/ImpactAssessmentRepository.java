package com.regintel.ai.enterpriseimpact.repository;

import com.regintel.ai.enterpriseimpact.entity.ImpactAssessment;
import com.regintel.ai.enterpriseimpact.entity.ImpactSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImpactAssessmentRepository extends JpaRepository<ImpactAssessment, UUID> {

    List<ImpactAssessment> findByRegulationAnalysis_Id(UUID regulationAnalysisId);

    List<ImpactAssessment> findBySeverity(ImpactSeverity severity);
}
