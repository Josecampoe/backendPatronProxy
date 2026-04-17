package com.aiproxy.backend.controller;

import com.aiproxy.backend.dto.GenerationRequest;
import com.aiproxy.backend.dto.GenerationResponse;
import com.aiproxy.backend.proxy.RateLimitProxyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final RateLimitProxyService rateLimitProxyService;

    @PostMapping("/generate")
    public ResponseEntity<GenerationResponse> generate(@Valid @RequestBody GenerationRequest request) {
        GenerationResponse response = rateLimitProxyService.generate(request);
        return ResponseEntity.ok(response);
    }
}
