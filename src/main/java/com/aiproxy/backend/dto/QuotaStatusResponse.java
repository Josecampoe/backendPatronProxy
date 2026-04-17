package com.aiproxy.backend.dto;

import com.aiproxy.backend.enums.UserPlan;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class QuotaStatusResponse {
    private long tokensUsed;
    private long tokensRemaining;
    private LocalDate resetDate;
    private UserPlan plan;
    private int requestsThisMinute;
    private int requestsLimit;
}
