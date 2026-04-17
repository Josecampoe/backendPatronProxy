package com.aiproxy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerationRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "prompt is required")
    private String prompt;
}
