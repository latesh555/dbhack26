package com.regintel.ai.engineeringplanning.repository;

import com.regintel.ai.engineeringplanning.entity.EngineeringTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EngineeringTaskRepository extends JpaRepository<EngineeringTask, UUID> {

    List<EngineeringTask> findByEngineeringPlan_Id(UUID engineeringPlanId);
}
