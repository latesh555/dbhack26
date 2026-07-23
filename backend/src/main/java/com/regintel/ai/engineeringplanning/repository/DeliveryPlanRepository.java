package com.regintel.ai.engineeringplanning.repository;

import com.regintel.ai.engineeringplanning.entity.DeliveryPlan;
import com.regintel.ai.engineeringplanning.entity.DeliveryPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryPlanRepository extends JpaRepository<DeliveryPlan, UUID> {

    Optional<DeliveryPlan> findFirstByRegulation_IdAndStatusOrderByGeneratedAtDesc(
            UUID regulationId, DeliveryPlanStatus status);

    Optional<DeliveryPlan> findFirstByRegulation_IdOrderByGeneratedAtDesc(UUID regulationId);
}
