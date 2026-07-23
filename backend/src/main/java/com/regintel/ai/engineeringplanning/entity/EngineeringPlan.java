package com.regintel.ai.engineeringplanning.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.enterpriseimpact.entity.ImpactAssessment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "engineering_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngineeringPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "impact_assessment_id", nullable = false)
    private ImpactAssessment impactAssessment;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    @Column(name = "estimated_effort_days")
    private Integer estimatedEffortDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private PlanStatus status = PlanStatus.DRAFT;

    @OneToMany(mappedBy = "engineeringPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EngineeringTask> tasks = new ArrayList<>();
}
