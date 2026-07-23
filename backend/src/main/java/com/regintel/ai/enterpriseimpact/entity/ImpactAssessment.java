package com.regintel.ai.enterpriseimpact.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "impact_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactAssessment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regulation_analysis_id", nullable = false)
    private RegulationAnalysis regulationAnalysis;

    @Column(name = "business_unit", nullable = false)
    private String businessUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "impact_type", nullable = false, length = 50)
    private ImpactType impactType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private ImpactSeverity severity = ImpactSeverity.MEDIUM;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "estimated_cost", precision = 15, scale = 2)
    private BigDecimal estimatedCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private ImpactStatus status = ImpactStatus.DRAFT;
}
