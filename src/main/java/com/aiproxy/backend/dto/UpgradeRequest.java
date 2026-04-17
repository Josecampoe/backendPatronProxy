package com.aiproxy.backend.dto;

import com.aiproxy.backend.enums.UserPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpgradeRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "targetPlan is required")
    private UserPlan targetPlan;
}
