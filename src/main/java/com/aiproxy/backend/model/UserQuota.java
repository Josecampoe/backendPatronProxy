package com.aiproxy.backend.model;

import com.aiproxy.backend.enums.UserPlan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_quota")
@Data
@NoArgsConstructor
public class UserQuota {

    @Id
    @Column(nullable = false, unique = true)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPlan plan = UserPlan.FREE;

    @Column(nullable = false)
    private long tokensUsedThisMonth = 0;

    @Column(nullable = false)
    private LocalDate quotaResetDate = LocalDate.now().withDayOfMonth(1).plusMonths(1);

    public UserQuota(String userId) {
        this.userId = userId;
    }

    public long getTokensRemaining() {
        long limit = plan.getMonthlyTokens();
        if (limit == Integer.MAX_VALUE) return Long.MAX_VALUE;
        return Math.max(0, limit - tokensUsedThisMonth);
    }
}
