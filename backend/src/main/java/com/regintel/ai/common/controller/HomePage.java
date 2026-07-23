package com.regintel.ai.common.controller;

import com.regintel.ai.common.dto.RegulationAnalysisModel;
import com.regintel.ai.common.dto.StepDetailsModel;
import com.regintel.ai.common.services.RegulationAnalysissService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(allowedHeaders = "**")
public class HomePage {

    private final RegulationAnalysissService regulationAnalysissService;

    public HomePage(RegulationAnalysissService regulationAnalysissService) {
        this.regulationAnalysissService = regulationAnalysissService;
    }

    @GetMapping("/analysis/{reqId}")
    public ResponseEntity<RegulationAnalysisModel> getRegulationAnalysis(
            @PathVariable Long reqId) {

        RegulationAnalysisModel response =
                regulationAnalysissService.getRegulationAnalysis(reqId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/step-details")
    public ResponseEntity<List<StepDetailsModel>> getStepDetails() {

        List<StepDetailsModel> response =
                regulationAnalysissService.getStepDetails();

        return ResponseEntity.ok(response);
    }
}