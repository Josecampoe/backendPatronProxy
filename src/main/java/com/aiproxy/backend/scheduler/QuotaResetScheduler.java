package com.aiproxy.backend.scheduler;

import com.aiproxy.backend.proxy.RateLimitProxyService;
import com.aiproxy.backend.service.QuotaManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotaResetScheduler {

    private final RateLimitProxyService rateLimitProxyService;
    private final QuotaManagementService quotaManagementService;

    @Value("${scheduler.rate-limit-reset-ms:60000}")
    private long rateLimitResetMs;

    @Scheduled(fixedRateString = "${scheduler.rate-limit-reset-ms:60000}")
    public void resetPerMinuteCounters() {
        rateLimitProxyService.resetCounters();
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void resetMonthlyQuotas() {
        quotaManagementService.resetMonthlyQuotas();
    }
}
