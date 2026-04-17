package com.aiproxy.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class QuotaHistoryResponse {
    private LocalDate date;
    private long tokensUsed;
    private int requestCount;
}
