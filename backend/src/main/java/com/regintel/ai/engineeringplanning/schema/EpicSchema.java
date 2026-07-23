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
public class EpicSchema {

    @NotBlank
    private String epicId;

    @NotBlank
    private String title;

    private String description;
    private Priority priority;
    private String ownerTeam;
    private Integer totalStoryPoints;

    @Builder.Default
    private List<FeatureSchema> features = new ArrayList<>();
}
