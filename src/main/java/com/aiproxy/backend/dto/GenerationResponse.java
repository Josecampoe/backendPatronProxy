package com.aiproxy.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerationResponse {
    private String text;
    private int tokensUsed;
    private long processingTimeMs;
}
