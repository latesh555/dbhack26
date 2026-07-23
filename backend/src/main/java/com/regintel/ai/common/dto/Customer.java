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
@Table(name = "Customer")
public class Customer {

    @Id
    private Long reqId;
    private Integer matches;
    private Integer pendingPayments;
    private Integer tradeFinanceDeals;
    private Integer corporateAccounts;
    private Integer swiftMessages;
    private Integer transactionsToRescreen;

    public Customer() {
    }

    // Getters & Setters
}
