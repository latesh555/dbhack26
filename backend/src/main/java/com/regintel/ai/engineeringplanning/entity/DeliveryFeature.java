package com.regintel.ai.engineeringplanning.entity;

import com.regintel.ai.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "delivery_features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryFeature extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "epic_id", nullable = false)
    private DeliveryEpic epic;

    @Column(name = "feature_key", nullable = false, length = 50)
    private String featureKey;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Priority priority;

    @Column(name = "owner_team", nullable = false)
    private String ownerTeam;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeliveryUserStory> userStories = new ArrayList<>();
}
