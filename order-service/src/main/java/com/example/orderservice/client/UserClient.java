package com.example.orderservice.client;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.orderservice.dto.UserResponse;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

@Component
public class UserClient {

    private final RestClient userRestClient;
    private final Retry retry;
    private final CircuitBreaker circuitBreaker;

    public UserClient(RestClient userRestClient) {

        this.userRestClient = userRestClient;

        // Retry configuration
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .build();

        this.retry = Retry.of("userServiceRetry", retryConfig);

        // Circuit Breaker configuration
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom()
                        .failureRateThreshold(50)
                        .minimumNumberOfCalls(4)
                        .slidingWindowSize(4)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(2)
                        .build();

        this.circuitBreaker =
                CircuitBreaker.of(
                        "userServiceCircuitBreaker",
                        circuitBreakerConfig
                );
    }

    public UserResponse getUserById(Long userId) {

        var decoratedCall =
                CircuitBreaker.decorateSupplier(
                        circuitBreaker,
                        Retry.decorateSupplier(
                                retry,
                                () -> userRestClient
                                        .get()
                                        .uri("/users/{id}", userId)
                                        .retrieve()
                                        .body(UserResponse.class)
                        )
                );

        try {

            return decoratedCall.get();

        } catch (CallNotPermittedException ex) {

            // Circuit breaker is OPEN
            return getFallbackUser(userId);

        } catch (Exception ex) {

            // User Service failed after retries
            return getFallbackUser(userId);
        }
    }

    // Fallback response
    private UserResponse getFallbackUser(Long userId) {

        return new UserResponse(
                userId,
                "Guest User",
                "guest@example.com"
        );
    }
}