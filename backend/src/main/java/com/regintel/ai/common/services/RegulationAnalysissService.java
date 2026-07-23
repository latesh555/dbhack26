package com.regintel.ai.common.services;

import com.regintel.ai.common.dto.*;
import com.regintel.ai.common.dto.StepDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegulationAnalysissService {

    private final SummaryRepository summaryRepository;
    private final CustomerImpactRepository customerImpactRepository;
    private final EnterpriseImpactRepository enterpriseImpactRepository;
    private final BusinessImpactRepository businessImpactRepository;
    private final StepDetailsRepository stepDetailsRepository;

    public RegulationAnalysissService(
            SummaryRepository summaryRepository,
            CustomerImpactRepository customerImpactRepository,
            EnterpriseImpactRepository enterpriseImpactRepository,
            BusinessImpactRepository businessImpactRepository,
            StepDetailsRepository stepDetailsRepository) {

        this.summaryRepository = summaryRepository;
        this.customerImpactRepository = customerImpactRepository;
        this.enterpriseImpactRepository = enterpriseImpactRepository;
        this.businessImpactRepository = businessImpactRepository;
        this.stepDetailsRepository = stepDetailsRepository;
    }

    public RegulationAnalysisModel getRegulationAnalysis(Long id) {

        Summary summary = summaryRepository.findById(id).orElse(null);

        Customer customerImpact =
                customerImpactRepository.findById(id).orElse(null);

        Enterprise enterpriseImpact =
                enterpriseImpactRepository.findById(id).orElse(null);

        Business businessImpact =
                businessImpactRepository.findById(id).orElse(null);

        return new RegulationAnalysisModel(
                summary,
                customerImpact,
                enterpriseImpact,
                businessImpact
        );
    }

    public List<StepDetailsModel> getStepDetails() {

        List<StepDetails> stepDetails =
                stepDetailsRepository.findAll();

        return stepDetails.stream()
                .map(step -> new StepDetailsModel(
                        step.getType(),
                        step.getSeverity(),
                        step.getName(),
                        step.getTime(),
                        step.getReqId()
                ))
                .collect(Collectors.toList());
    }
}