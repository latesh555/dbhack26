package com.regintel.ai.engineeringplanning.schema;

import com.regintel.ai.engineeringplanning.entity.Priority;
import com.regintel.ai.engineeringplanning.entity.UserStoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStorySchema {

    private UUID id;

    @NotBlank
    private String storyId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Priority priority;

    @NotNull
    private Integer storyPoints;

    @NotBlank
    private String ownerTeam;

    @Builder.Default
    private List<String> dependencies = new ArrayList<>();

    @Builder.Default
    private List<String> affectedComponents = new ArrayList<>();

    @Builder.Default
    private List<String> acceptanceCriteria = new ArrayList<>();

    @Builder.Default
    private List<String> testingChecklist = new ArrayList<>();

    @Builder.Default
    private List<StoryTaskSchema> tasks = new ArrayList<>();

    private UserStoryStatus status;
    private String jiraIssueKey;
}
