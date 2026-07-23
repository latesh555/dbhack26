package com.regintel.ai.engineeringplanning.repository;

import com.regintel.ai.engineeringplanning.entity.DeliveryUserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryUserStoryRepository extends JpaRepository<DeliveryUserStory, UUID> {

    Optional<DeliveryUserStory> findByStoryKey(String storyKey);

    List<DeliveryUserStory> findByFeature_Epic_DeliveryPlan_Regulation_Id(UUID regulationId);
}
