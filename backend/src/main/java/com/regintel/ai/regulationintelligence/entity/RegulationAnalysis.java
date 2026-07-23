package com.regintel.ai.regulationintelligence.entity;

import com.regintel.ai.common.entity.BaseEntity;
import com.regintel.ai.regulation.entity.Regulation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "regulation_analyses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegulationAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regulation_id", nullable = false)
    private Regulation regulation;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "key_requirements", columnDefinition = "TEXT")
    private String keyRequirements;

    @Column(name = "compliance_areas", columnDefinition = "TEXT")
    private String complianceAreas;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 50)
    @Builder.Default
    private RiskLevel riskLevel = RiskLevel.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private AnalysisStatus status = AnalysisStatus.PENDING;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    @Column(name = "intelligence_payload", columnDefinition = "TEXT")
    private String intelligencePayload;
}
