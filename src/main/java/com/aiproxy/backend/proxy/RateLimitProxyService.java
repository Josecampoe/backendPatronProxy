package com.aiproxy.backend.proxy;

import com.aiproxy.backend.dto.GenerationRequest;
import com.aiproxy.backend.dto.GenerationResponse;
import com.aiproxy.backend.exception.RateLimitExceededException;
import com.aiproxy.backend.repository.UserQuotaRepository;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitProxyService implements AIGenerationService {

    private final long windowMs;
    private final AIGenerationService next;
    private final UserQuotaRepository userQuotaRepository;
    private final ConcurrentHashMap<String, Deque<Long>> requestTimestamps = new ConcurrentHashMap<>();

    public RateLimitProxyService(AIGenerationService next,
                                  UserQuotaRepository userQuotaRepository,
                                  long windowMs) {
        this.next = next;
        this.userQuotaRepository = userQuotaRepository;
        this.windowMs = windowMs;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        String userId = request.getUserId();
        int limit = getUserLimit(userId);
        long now = Instant.now().toEpochMilli();

        Deque<Long> timestamps = requestTimestamps.computeIfAbsent(userId, k -> new ArrayDeque<>());

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && now - timestamps.peekFirst() > windowMs) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= limit) {
                long oldestInWindow = timestamps.peekFirst();
                int retryAfter = (int) Math.ceil((windowMs - (now - oldestInWindow)) / 1000.0);
                throw new RateLimitExceededException(Math.max(1, retryAfter));
            }
            timestamps.addLast(now);
        }

        return next.generate(request);
    }

    public int getCurrentRequestCount(String userId) {
        long now = Instant.now().toEpochMilli();
        Deque<Long> timestamps = requestTimestamps.getOrDefault(userId, new ArrayDeque<>());
        synchronized (timestamps) {
            timestamps.removeIf(t -> now - t > windowMs);
            return timestamps.size();
        }
    }

    public void resetCounters() {
        requestTimestamps.clear();
    }

    private int getUserLimit(String userId) {
        return userQuotaRepository.findById(userId)
                .map(uq -> uq.getPlan().getRequestsPerMinute())
                .orElse(10);
    }
}
