package com.aiproxy.backend.enums;

public enum UserPlan {
    FREE(10, 50_000),
    PRO(60, 500_000),
    ENTERPRISE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int requestsPerMinute;
    private final int monthlyTokens;

    UserPlan(int requestsPerMinute, int monthlyTokens) {
        this.requestsPerMinute = requestsPerMinute;
        this.monthlyTokens = monthlyTokens;
    }

    public int getRequestsPerMinute() { return requestsPerMinute; }
    public int getMonthlyTokens() { return monthlyTokens; }
}
