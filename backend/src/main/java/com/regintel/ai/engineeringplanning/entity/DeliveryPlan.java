package com.regintel.ai.engineeringplanning.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.enterpriseimpact.entity.EnterpriseImpactAssessment;
import com.regintel.ai.regulation.entity.Regulation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "delivery_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enterprise_impact_assessment_id", nullable = false)
    private EnterpriseImpactAssessment enterpriseImpactAssessment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regulation_id", nullable = false)
    private Regulation regulation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private DeliveryPlanStatus status = DeliveryPlanStatus.GENERATING;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "affected_apis", columnDefinition = "TEXT")
    private String affectedApis;

    @Column(name = "affected_microservices", columnDefinition = "TEXT")
    private String affectedMicroservices;

    @Column(name = "testing_strategy", columnDefinition = "TEXT")
    private String testingStrategy;

    @Column(name = "deployment_strategy", columnDefinition = "TEXT")
    private String deploymentStrategy;

    @Column(name = "rollback_strategy", columnDefinition = "TEXT")
    private String rollbackStrategy;

    @Column(name = "production_validation_checklist", columnDefinition = "TEXT")
    private String productionValidationChecklist;

    @Column(name = "jira_sync_reference")
    private String jiraSyncReference;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @OneToMany(mappedBy = "deliveryPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeliveryEpic> epics = new ArrayList<>();
}
