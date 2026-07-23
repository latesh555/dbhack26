package com.regintel.ai.engineeringplanning.dto;

import com.regintel.ai.engineeringplanning.entity.UserStoryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStoryStatusRequest {

    @NotNull(message = "Status is required")
    private UserStoryStatus status;
}
