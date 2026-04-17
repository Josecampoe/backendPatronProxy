package com.aiproxy.backend.repository;

import com.aiproxy.backend.model.DailyUsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyUsageRepository extends JpaRepository<DailyUsageRecord, Long> {

    Optional<DailyUsageRecord> findByUserIdAndDate(String userId, LocalDate date);

    List<DailyUsageRecord> findByUserIdAndDateBetweenOrderByDateAsc(
            String userId, LocalDate from, LocalDate to);
}
