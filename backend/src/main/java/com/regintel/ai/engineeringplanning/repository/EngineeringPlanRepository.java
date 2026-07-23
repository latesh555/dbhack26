package com.regintel.ai.engineeringplanning.repository;

import com.regintel.ai.engineeringplanning.entity.EngineeringPlan;
import com.regintel.ai.engineeringplanning.entity.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EngineeringPlanRepository extends JpaRepository<EngineeringPlan, UUID> {

    List<EngineeringPlan> findByImpactAssessment_Id(UUID impactAssessmentId);

    List<EngineeringPlan> findByStatus(PlanStatus status);
}
