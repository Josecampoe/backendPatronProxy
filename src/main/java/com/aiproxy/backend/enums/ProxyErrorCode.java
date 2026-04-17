package com.aiproxy.backend.enums;

public enum ProxyErrorCode {
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", "Too many requests. Please wait before retrying."),
    QUOTA_EXCEEDED("QUOTA_EXCEEDED", "Monthly token quota exhausted. Please upgrade your plan.");

    private final String code;
    private final String message;

    ProxyErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}
