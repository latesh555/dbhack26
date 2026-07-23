package com.regintel.ai.enterpriseimpact.repository;

import com.regintel.ai.enterpriseimpact.entity.EnterpriseAssessmentStatus;
import com.regintel.ai.enterpriseimpact.entity.EnterpriseImpactAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnterpriseImpactAssessmentRepository extends JpaRepository<EnterpriseImpactAssessment, UUID> {

    Optional<EnterpriseImpactAssessment> findFirstByRegulation_IdOrderByCreatedAtDesc(UUID regulationId);

    Optional<EnterpriseImpactAssessment> findFirstByRegulation_IdAndStatusOrderByAnalyzedAtDesc(
            UUID regulationId, EnterpriseAssessmentStatus status);

    List<EnterpriseImpactAssessment> findByRegulation_Id(UUID regulationId);
}
