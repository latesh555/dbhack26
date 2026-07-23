package com.regintel.ai.agentorchestration.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.regulation.entity.Regulation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "agent_workflows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentWorkflow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "workflow_type", nullable = false, length = 100)
    private String workflowType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private WorkflowStatus status = WorkflowStatus.PENDING;

    @Column(name = "current_step", length = 100)
    private String currentStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulation_id")
    private Regulation regulation;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AgentTask> tasks = new ArrayList<>();
}
