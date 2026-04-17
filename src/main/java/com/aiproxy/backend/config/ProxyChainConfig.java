package com.aiproxy.backend.config;

import com.aiproxy.backend.proxy.*;
import com.aiproxy.backend.repository.DailyUsageRepository;
import com.aiproxy.backend.repository.UserQuotaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ProxyChainConfig {

    /**
     * Wires the proxy chain:
     * request → RateLimitProxyService → QuotaProxyService → MockAIGenerationService
     */
    @Bean
    public RateLimitProxyService rateLimitProxyService(
            MockAIGenerationService mock,
            UserQuotaRepository userQuotaRepository,
            DailyUsageRepository dailyUsageRepository) {

        QuotaProxyService quotaProxy = new QuotaProxyService(mock, userQuotaRepository, dailyUsageRepository);
        return new RateLimitProxyService(quotaProxy, userQuotaRepository);
    }
}
