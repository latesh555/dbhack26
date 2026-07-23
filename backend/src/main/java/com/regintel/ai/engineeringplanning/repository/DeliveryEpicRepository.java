package com.regintel.ai.engineeringplanning.repository;

import com.regintel.ai.engineeringplanning.entity.DeliveryEpic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryEpicRepository extends JpaRepository<DeliveryEpic, UUID> {

    List<DeliveryEpic> findByDeliveryPlan_Id(UUID deliveryPlanId);
}
