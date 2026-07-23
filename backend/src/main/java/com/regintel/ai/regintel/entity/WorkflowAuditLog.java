package com.regintel.ai.regintel.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.agentorchestration.entity.AgentWorkflow;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowAuditLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_id", nullable = false)
    private AgentWorkflow workflow;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_name", length = 100)
    private WorkflowStep stepName;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private AuditEventType eventType;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}
