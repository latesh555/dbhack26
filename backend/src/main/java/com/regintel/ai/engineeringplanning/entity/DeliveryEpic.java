package com.regintel.ai.engineeringplanning.entity;

import com.regintel.ai.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "delivery_epics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryEpic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_plan_id", nullable = false)
    private DeliveryPlan deliveryPlan;

    @Column(name = "epic_key", nullable = false, length = 50)
    private String epicKey;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Priority priority;

    @Column(name = "owner_team", nullable = false)
    private String ownerTeam;

    @Column(name = "total_story_points")
    private Integer totalStoryPoints;

    @OneToMany(mappedBy = "epic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeliveryFeature> features = new ArrayList<>();
}
