package com.aiproxy.backend.repository;

import com.aiproxy.backend.model.UserQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuotaRepository extends JpaRepository<UserQuota, String> {
}
