package com.aiproxy.backend.exception;

public class QuotaExceededException extends RuntimeException {

    public QuotaExceededException(String userId) {
        super("Monthly token quota exceeded for user: " + userId);
    }
}
