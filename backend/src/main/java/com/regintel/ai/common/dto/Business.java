package com.regintel.ai.common.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Table(name = "Business")
public class Business {

    @Id
    private Long reqId;
    private String revenueImpactPerDay;
    private String settlementDelay;
    private Integer operationsReviewCount;
    private Integer investigationHours;
    private String criticality;
    private Double paymentDisruptionPercentage;

    public Business() {
    }

    // Getters & Setters
}