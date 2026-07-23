package com.regintel.ai.enterpriseimpact.entity;

import com.regintel.ai.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "enterprise_impact_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnterpriseImpactItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private EnterpriseImpactAssessment assessment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ImpactCategory category;

    @Column(name = "component_name", nullable = false)
    private String componentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, length = 50)
    private ComponentType componentType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ImpactSeverity severity;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal confidence;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String evidence;
}
