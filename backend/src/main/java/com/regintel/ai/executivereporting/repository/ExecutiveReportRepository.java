package com.regintel.ai.executivereporting.repository;

import com.regintel.ai.executivereporting.entity.ExecutiveReport;
import com.regintel.ai.executivereporting.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutiveReportRepository extends JpaRepository<ExecutiveReport, UUID> {

    List<ExecutiveReport> findByReportType(ReportType reportType);

    List<ExecutiveReport> findByRegulation_Id(UUID regulationId);
}
