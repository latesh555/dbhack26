package com.regintel.ai.engineeringplanning.entity;

import com.regintel.ai.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "delivery_story_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryStoryTask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_story_id", nullable = false)
    private DeliveryUserStory userStory;

    @Column(name = "task_key", nullable = false, length = 50)
    private String taskKey;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Priority priority;

    @Column(name = "owner_team", nullable = false)
    private String ownerTeam;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "TODO";
}
