package com.aiproxy.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "daily_usage_record")
@Data
@NoArgsConstructor
public class DailyUsageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private long tokensUsed = 0;

    @Column(nullable = false)
    private int requestCount = 0;

    public DailyUsageRecord(String userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }
}
