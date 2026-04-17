package com.aiproxy.backend.controller;

import com.aiproxy.backend.dto.QuotaHistoryResponse;
import com.aiproxy.backend.dto.QuotaStatusResponse;
import com.aiproxy.backend.dto.UpgradeRequest;
import com.aiproxy.backend.service.QuotaManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quota")
@RequiredArgsConstructor
public class QuotaController {

    private final QuotaManagementService quotaManagementService;

    @GetMapping("/status")
    public ResponseEntity<QuotaStatusResponse> getStatus(@RequestParam String userId) {
        return ResponseEntity.ok(quotaManagementService.getStatus(userId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<QuotaHistoryResponse>> getHistory(@RequestParam String userId) {
        return ResponseEntity.ok(quotaManagementService.getHistory(userId));
    }

    @PostMapping("/upgrade")
    public ResponseEntity<Map<String, String>> upgrade(@Valid @RequestBody UpgradeRequest request) {
        quotaManagementService.upgradePlan(request.getUserId(), request.getTargetPlan());
        return ResponseEntity.ok(Map.of(
                "message", "Plan upgraded successfully",
                "userId", request.getUserId(),
                "newPlan", request.getTargetPlan().name()
        ));
    }
}
