package com.regintel.ai.enterpriseimpact.repository;

import com.regintel.ai.enterpriseimpact.entity.EnterpriseImpactItem;
import com.regintel.ai.enterpriseimpact.entity.ImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnterpriseImpactItemRepository extends JpaRepository<EnterpriseImpactItem, UUID> {

    List<EnterpriseImpactItem> findByAssessment_IdAndCategory(UUID assessmentId, ImpactCategory category);

    List<EnterpriseImpactItem> findByAssessment_Id(UUID assessmentId);
}
