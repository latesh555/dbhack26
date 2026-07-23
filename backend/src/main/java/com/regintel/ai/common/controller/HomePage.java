package com.regintel.ai.common.controller;

import com.regintel.ai.common.dto.RegulationAnalysisModel;
import com.regintel.ai.common.services.RegulationAnalysissService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequiredArgsConstructor
public class HomePage {
//    @RestController
//    @RequiredArgsConstructor
//    public class RegulationAnalysisController {
        private final RegulationAnalysissService regulationAnalysissService;

    public HomePage(RegulationAnalysissService regulationAnalysissService) {
        this.regulationAnalysissService = regulationAnalysissService;
    }

    @GetMapping("/analysis/{reqId}")
        public ResponseEntity<RegulationAnalysisModel> getRegulationAnalysis(@PathVariable Long reqId) {

            RegulationAnalysisModel response =
                    regulationAnalysissService.getRegulationAnalysis(reqId);

            return ResponseEntity.ok(response);
        }

//    }
}
