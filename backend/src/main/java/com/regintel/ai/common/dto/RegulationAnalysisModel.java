package com.regintel.ai.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RegulationAnalysisModel {

    private Summary summary;
    private Customer customer;
    private Enterprise enterprise;
    private Business business;

    public RegulationAnalysisModel() {
    }


    // Getters & Setters
}