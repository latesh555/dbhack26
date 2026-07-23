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
@Table(name = "Enterprise")
public class Enterprise {

    @Id
    private Long reqId;
    private Integer applications;
    private String tradeFinanceRisk;
    private String swiftGatewayRisk;
    private Integer customerOnboardingHours;
    private Integer tradeSettlementsAffected;
    private String amlMonitoring;

    public Enterprise() {
    }

    // Getters & Setters
}
