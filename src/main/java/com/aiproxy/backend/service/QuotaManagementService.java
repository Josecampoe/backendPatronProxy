package com.aiproxy.backend.service;

import com.aiproxy.backend.dto.QuotaHistoryResponse;
import com.aiproxy.backend.dto.QuotaStatusResponse;
import com.aiproxy.backend.enums.UserPlan;
import com.aiproxy.backend.model.DailyUsageRecord;
import com.aiproxy.backend.model.UserQuota;
import com.aiproxy.backend.proxy.RateLimitProxyService;
import com.aiproxy.backend.repository.DailyUsageRepository;
import com.aiproxy.backend.repository.UserQuotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotaManagementService {

    private final UserQuotaRepository userQuotaRepository;
    private final DailyUsageRepository dailyUsageRepository;
    private final RateLimitProxyService rateLimitProxyService;

    public QuotaStatusResponse getStatus(String userId) {
        UserQuota quota = userQuotaRepository.findById(userId)
                .orElseGet(() -> userQuotaRepository.save(new UserQuota(userId)));

        int requestsThisMinute = rateLimitProxyService.getCurrentRequestCount(userId);
        long tokensRemaining = quota.getTokensRemaining();

        return new QuotaStatusResponse(
                quota.getTokensUsedThisMonth(),
                tokensRemaining == Long.MAX_VALUE ? -1 : tokensRemaining,
                quota.getQuotaResetDate(),
                quota.getPlan(),
                requestsThisMinute,
                quota.getPlan().getRequestsPerMinute()
        );
    }

    public List<QuotaHistoryResponse> getHistory(String userId) {
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(6);
        List<DailyUsageRecord> records = dailyUsageRepository
                .findByUserIdAndDateBetweenOrderByDateAsc(userId, from, to);
        return records.stream()
                .map(r -> new QuotaHistoryResponse(r.getDate(), r.getTokensUsed(), r.getRequestCount()))
                .collect(Collectors.toList());
    }

    public UserQuota upgradePlan(String userId, UserPlan targetPlan) {
        UserQuota quota = userQuotaRepository.findById(userId)
                .orElseGet(() -> new UserQuota(userId));
        quota.setPlan(targetPlan);
        return userQuotaRepository.save(quota);
    }

    public void resetMonthlyQuotas() {
        List<UserQuota> all = userQuotaRepository.findAll();
        all.forEach(q -> {
            q.setTokensUsedThisMonth(0);
            q.setQuotaResetDate(LocalDate.now().withDayOfMonth(1).plusMonths(1));
        });
        userQuotaRepository.saveAll(all);
    }
}
