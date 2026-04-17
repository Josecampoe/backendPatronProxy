package com.aiproxy.backend.proxy;

import com.aiproxy.backend.dto.GenerationRequest;
import com.aiproxy.backend.dto.GenerationResponse;
import com.aiproxy.backend.exception.QuotaExceededException;
import com.aiproxy.backend.model.DailyUsageRecord;
import com.aiproxy.backend.model.UserQuota;
import com.aiproxy.backend.repository.DailyUsageRepository;
import com.aiproxy.backend.repository.UserQuotaRepository;

import java.time.LocalDate;

public class QuotaProxyService implements AIGenerationService {

    private final AIGenerationService next;
    private final UserQuotaRepository userQuotaRepository;
    private final DailyUsageRepository dailyUsageRepository;

    public QuotaProxyService(AIGenerationService next,
                              UserQuotaRepository userQuotaRepository,
                              DailyUsageRepository dailyUsageRepository) {
        this.next = next;
        this.userQuotaRepository = userQuotaRepository;
        this.dailyUsageRepository = dailyUsageRepository;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        String userId = request.getUserId();
        UserQuota quota = userQuotaRepository.findById(userId)
                .orElseGet(() -> userQuotaRepository.save(new UserQuota(userId)));

        if (quota.getTokensRemaining() <= 0) {
            throw new QuotaExceededException(userId);
        }

        GenerationResponse response = next.generate(request);

        // Deduct tokens and persist
        quota.setTokensUsedThisMonth(quota.getTokensUsedThisMonth() + response.getTokensUsed());
        userQuotaRepository.save(quota);

        // Update daily record
        DailyUsageRecord daily = dailyUsageRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .orElseGet(() -> new DailyUsageRecord(userId, LocalDate.now()));
        daily.setTokensUsed(daily.getTokensUsed() + response.getTokensUsed());
        daily.setRequestCount(daily.getRequestCount() + 1);
        dailyUsageRepository.save(daily);

        return response;
    }
}
