package com.aiproxy.backend.scheduler;

import com.aiproxy.backend.proxy.RateLimitProxyService;
import com.aiproxy.backend.service.QuotaManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotaResetScheduler {

    private final RateLimitProxyService rateLimitProxyService;
    private final QuotaManagementService quotaManagementService;

    // Reset per-minute counters every 60 seconds
    @Scheduled(fixedRate = 60000)
    public void resetPerMinuteCounters() {
        rateLimitProxyService.resetCounters();
    }

    // Reset monthly quota on the 1st of each month at midnight
    @Scheduled(cron = "0 0 0 1 * *")
    public void resetMonthlyQuotas() {
        quotaManagementService.resetMonthlyQuotas();
    }
}
