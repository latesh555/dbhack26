package com.regintel.ai.regulation.repository;

import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.entity.RegulationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegulationRepository extends JpaRepository<Regulation, UUID> {

    List<Regulation> findByStatus(RegulationStatus status);

    List<Regulation> findByJurisdiction(String jurisdiction);
}
