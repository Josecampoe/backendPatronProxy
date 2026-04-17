package com.aiproxy.backend.proxy;

import com.aiproxy.backend.dto.GenerationRequest;
import com.aiproxy.backend.dto.GenerationResponse;
import com.aiproxy.backend.exception.QuotaExceededException;
import com.aiproxy.backend.exception.RateLimitExceededException;

public interface AIGenerationService {
    GenerationResponse generate(GenerationRequest request)
            throws RateLimitExceededException, QuotaExceededException;
}
