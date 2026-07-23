package com.regintel.ai.regulation.entity;

import com.regintel.ai.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "regulations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regulation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false, length = 100)
    private String jurisdiction;

    @Column(name = "document_type", nullable = false, length = 100)
    private String documentType;

    @Column(name = "raw_content", columnDefinition = "TEXT")
    private String rawContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private RegulationStatus status = RegulationStatus.DRAFT;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;
}
