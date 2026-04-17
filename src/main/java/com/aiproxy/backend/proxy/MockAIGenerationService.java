package com.aiproxy.backend.proxy;

import com.aiproxy.backend.dto.GenerationRequest;
import com.aiproxy.backend.dto.GenerationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MockAIGenerationService implements AIGenerationService {

    private static final List<String> RESPONSES = List.of(
        "The Proxy pattern provides a surrogate or placeholder for another object to control access to it.",
        "Design patterns are reusable solutions to commonly occurring problems in software design.",
        "Rate limiting is a technique used to control the rate of requests a user can make to an API.",
        "Spring Boot simplifies the development of production-ready applications with the Spring framework.",
        "Microservices architecture structures an application as a collection of small autonomous services.",
        "The chain of responsibility pattern passes requests along a chain of handlers.",
        "Dependency injection is a technique where an object receives its dependencies from external sources.",
        "RESTful APIs use HTTP methods to perform CRUD operations on resources.",
        "Token-based authentication uses signed tokens to verify the identity of users.",
        "Caching improves performance by storing frequently accessed data in fast storage.",
        "Event-driven architecture uses events to trigger and communicate between decoupled services.",
        "The observer pattern defines a one-to-many dependency between objects."
    );

    @Value("${mock.ai.latency-ms:1200}")
    private long latencyMs;

    private final Random random = new Random();

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(latencyMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String responseText = RESPONSES.get(random.nextInt(RESPONSES.size()));
        int tokens = calculateTokens(request.getPrompt(), responseText);
        return new GenerationResponse(responseText, tokens, System.currentTimeMillis() - start);
    }

    private int calculateTokens(String prompt, String response) {
        int promptWords = prompt.trim().split("\\s+").length;
        int responseWords = response.trim().split("\\s+").length;
        return (int) (promptWords * 1.3) + responseWords;
    }
}
