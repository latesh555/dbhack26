package com.regintel.ai.enterpriseimpact.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.regulation.entity.Regulation;
import com.regintel.ai.regulationintelligence.entity.RegulationAnalysis;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "enterprise_impact_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnterpriseImpactAssessment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regulation_id", nullable = false)
    private Regulation regulation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regulation_analysis_id", nullable = false)
    private RegulationAnalysis regulationAnalysis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private EnterpriseAssessmentStatus status = EnterpriseAssessmentStatus.PENDING;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    @Column(name = "compliance_risk_summary", columnDefinition = "TEXT")
    private String complianceRiskSummary;

    @Column(name = "risk_heatmap", columnDefinition = "TEXT")
    private String riskHeatmap;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EnterpriseImpactItem> impactItems = new ArrayList<>();
}
