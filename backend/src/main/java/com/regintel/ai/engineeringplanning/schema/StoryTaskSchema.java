package com.regintel.ai.engineeringplanning.schema;

import com.regintel.ai.engineeringplanning.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryTaskSchema {

    @NotBlank
    private String taskId;

    @NotBlank
    private String title;

    private String description;
    private Priority priority;
    private String ownerTeam;
    private String status;
}
