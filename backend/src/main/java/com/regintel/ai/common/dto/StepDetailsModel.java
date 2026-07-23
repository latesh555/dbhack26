package com.regintel.ai.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepDetailsModel {

    private String type;

    private String severity;

    private String name;

    private LocalDateTime time;

    private Long reqId;
}