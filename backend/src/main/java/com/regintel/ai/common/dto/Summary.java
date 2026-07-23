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
@Table(name = "summary")
public class Summary {

    @Id
    private Long reqId;
    private String type;
    private String published;
    private String effectiveDate;
    private String deadline;
    private String severity;
    private Double confidence;
    private String summary;

    public Summary() {
    }

    public Summary(String type, String published, String effectiveDate,
                   String deadline, String severity,
                   Double confidence, String summary) {
        this.type = type;
        this.published = published;
        this.effectiveDate = effectiveDate;
        this.deadline = deadline;
        this.severity = severity;
        this.confidence = confidence;
        this.summary = summary;
    }

    // Getters & Setters
}