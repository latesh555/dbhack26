package com.regintel.ai.engineeringplanning.entity;

import com.regintel.ai.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "delivery_user_stories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryUserStory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feature_id", nullable = false)
    private DeliveryFeature feature;

    @Column(name = "story_key", nullable = false, length = 50)
    private String storyKey;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Priority priority;

    @Column(name = "story_points", nullable = false)
    private Integer storyPoints;

    @Column(name = "owner_team", nullable = false)
    private String ownerTeam;

    @Column(columnDefinition = "TEXT")
    private String dependencies;

    @Column(name = "affected_components", columnDefinition = "TEXT")
    private String affectedComponents;

    @Column(name = "acceptance_criteria", columnDefinition = "TEXT")
    private String acceptanceCriteria;

    @Column(name = "testing_checklist", columnDefinition = "TEXT")
    private String testingChecklist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private UserStoryStatus status = UserStoryStatus.TODO;

    @Column(name = "jira_issue_key")
    private String jiraIssueKey;

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeliveryStoryTask> tasks = new ArrayList<>();
}
