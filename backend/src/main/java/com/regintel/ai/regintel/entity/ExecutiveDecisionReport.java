package com.regintel.ai.regintel.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.agentorchestration.entity.AgentWorkflow;
import com.regintel.ai.regulation.entity.Regulation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "executive_decision_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutiveDecisionReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_id", nullable = false, unique = true)
    private AgentWorkflow workflow;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regulation_id", nullable = false)
    private Regulation regulation;

    @Column(name = "report_payload", columnDefinition = "TEXT", nullable = false)
    private String reportPayload;

    @Column(name = "overall_risk_score", precision = 5, scale = 2)
    private BigDecimal overallRiskScore;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
