package com.regintel.ai.regulationintelligence.repository;

import com.regintel.ai.regulationintelligence.entity.AnalysisStatus;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegulationAnalysisRepository extends JpaRepository<RegulationAnalysis, UUID> {

    List<RegulationAnalysis> findByRegulation_Id(UUID regulationId);

    List<RegulationAnalysis> findByStatus(AnalysisStatus status);
}
