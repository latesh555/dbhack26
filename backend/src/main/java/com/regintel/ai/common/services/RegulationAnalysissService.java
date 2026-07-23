package com.regintel.ai.common.services;

import com.regintel.ai.common.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegulationAnalysissService {


    private final SummaryRepository summaryRepository;
    private final CustomerImpactRepository customerImpactRepository;
    private final EnterpriseImpactRepository enterpriseImpactRepository;
    private final BusinessImpactRepository businessImpactRepository;

    public RegulationAnalysissService(SummaryRepository summaryRepository, CustomerImpactRepository customerImpactRepository, EnterpriseImpactRepository enterpriseImpactRepository, BusinessImpactRepository businessImpactRepository) {
        this.summaryRepository = summaryRepository;
        this.customerImpactRepository = customerImpactRepository;
        this.enterpriseImpactRepository = enterpriseImpactRepository;
        this.businessImpactRepository = businessImpactRepository;
    }

    public RegulationAnalysisModel getRegulationAnalysis(Long id) {

        Summary summary = summaryRepository.findById(id)
                .orElse(null);

        Customer customerImpact =
                customerImpactRepository.findById(id)
                        .orElse(null);

        Enterprise enterpriseImpact =
                enterpriseImpactRepository.findById(id)
                        .orElse(null);

        Business businessImpact =
                businessImpactRepository.findById(id)
                        .orElse(null);

        return new RegulationAnalysisModel(
                summary,
                customerImpact,
                enterpriseImpact,
                businessImpact
        );
    }
}