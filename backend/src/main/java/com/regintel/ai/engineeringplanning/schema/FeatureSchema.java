package com.regintel.ai.engineeringplanning.schema;

import com.regintel.ai.engineeringplanning.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureSchema {

    @NotBlank
    private String featureId;

    @NotBlank
    private String title;

    private String description;
    private Priority priority;
    private String ownerTeam;

    @Builder.Default
    private List<UserStorySchema> userStories = new ArrayList<>();
}
