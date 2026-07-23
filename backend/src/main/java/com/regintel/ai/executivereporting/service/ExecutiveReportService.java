package com.regintel.ai.executivereporting.service;

import com.regintel.ai.common.exception.ResourceNotFoundException;
import com.regintel.ai.executivereporting.dto.ExecutiveReportDto;
import com.regintel.ai.executivereporting.entity.ExecutiveReport;
import com.regintel.ai.executivereporting.entity.ReportStatus;
import com.regintel.ai.executivereporting.repository.ExecutiveReportRepository;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulation.service.RegulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutiveReportService {

    private final ExecutiveReportRepository reportRepository;
    private final RegulationService regulationService;

    @Transactional
    public ExecutiveReportDto.Response create(ExecutiveReportDto.Request request) {
        log.info("Creating executive report: {}", request.getTitle());
        Regulation regulation = null;
        if (request.getRegulationId() != null) {
            regulation = regulationService.getEntity(request.getRegulationId());
        }
        ExecutiveReport report = ExecutiveReport.builder()
                .title(request.getTitle())
                .reportType(request.getReportType())
                .content(request.getContent())
                .regulation(regulation)
                .status(request.getStatus() != null ? request.getStatus() : ReportStatus.DRAFT)
                .generatedAt(LocalDateTime.now())
                .build();
        return toResponse(reportRepository.save(report));
    }

    @Transactional(readOnly = true)
    public List<ExecutiveReportDto.Response> findAll() {
        return reportRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ExecutiveReportDto.Response findById(UUID id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ExecutiveReportDto.Response publish(UUID id) {
        log.info("Publishing executive report: {}", id);
        ExecutiveReport report = getEntity(id);
        report.setStatus(ReportStatus.PUBLISHED);
        report.setGeneratedAt(LocalDateTime.now());
        return toResponse(reportRepository.save(report));
    }

    public ExecutiveReport getEntity(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExecutiveReport", id));
    }

    private ExecutiveReportDto.Response toResponse(ExecutiveReport report) {
        return ExecutiveReportDto.Response.builder()
                .id(report.getId())
                .title(report.getTitle())
                .reportType(report.getReportType())
                .content(report.getContent())
                .regulationId(report.getRegulation() != null ? report.getRegulation().getId() : null)
                .status(report.getStatus())
                .generatedAt(report.getGeneratedAt())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }
}
