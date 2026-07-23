package com.regintel.ai.common.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "STEP_DETAILS")
@Data
public class StepDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REQ_ID")
    private Long reqId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SEVERITY")
    private String severity;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TIME")
    private LocalDateTime time;
}