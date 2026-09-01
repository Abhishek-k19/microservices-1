package com.example.orderservice.client;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.example.orderservice.dto.UserResponse;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserClient {

    private final RestClient userRestClient;
    private final Retry retry;
    private final CircuitBreaker circuitBreaker;
    private final HttpServletRequest request;

    public UserClient(
            RestClient userRestClient,
            HttpServletRequest request) {

        this.userRestClient = userRestClient;
        this.request = request;

        // Retry configuration
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(500))
                .build();

        this.retry = Retry.of(
                "userServiceRetry",
                retryConfig
        );

        // Circuit Breaker configuration
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom()
                        .failureRateThreshold(50)
                        .minimumNumberOfCalls(4)
                        .slidingWindowSize(4)
                        .waitDurationInOpenState(
                                Duration.ofSeconds(10)
                        )
                        .permittedNumberOfCallsInHalfOpenState(2)
                        .build();

        this.circuitBreaker =
                CircuitBreaker.of(
                        "userServiceCircuitBreaker",
                        circuitBreakerConfig
                );

        // Circuit Breaker state logging
        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        System.out.println(
                                "CIRCUIT BREAKER: "
                                        + event.getStateTransition()
                        )
                );
    }

    public UserResponse getUserById(Long userId) {

        // Get correlation ID from incoming request
        String correlationId =
                request.getHeader("X-Correlation-ID");

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = "NO-CORRELATION-ID";
        }

        final String finalCorrelationId = correlationId;

        System.out.println(
                "ORDER SERVICE → USER SERVICE | Correlation ID: "
                        + finalCorrelationId
        );

        // Circuit Breaker + Retry
        var decoratedCall =
                CircuitBreaker.decorateSupplier(
                        circuitBreaker,
                        Retry.decorateSupplier(
                                retry,
                                () -> userRestClient
                                        .get()
                                        .uri(
                                                "/users/{id}",
                                                userId
                                        )
                                        .header(
                                                "X-Correlation-ID",
                                                finalCorrelationId
                                        )
                                        .retrieve()
                                        .body(UserResponse.class)
                        )
                );

        try {

            System.out.println(
                    "Circuit Breaker State: "
                            + circuitBreaker.getState()
            );

            return decoratedCall.get();

        } catch (CallNotPermittedException ex) {

            // Circuit is OPEN
            System.out.println(
                    "Circuit Breaker is OPEN - using fallback"
            );

            return getFallbackUser(userId);

        } catch (HttpClientErrorException.NotFound ex) {

            // User does not exist
            System.out.println(
                    "USER NOT FOUND: " + userId
            );

            throw new RuntimeException(
                    "User not found with id: " + userId
            );

        } catch (Exception ex) {

            // Other failures:
            // timeout, connection refused, etc.
            System.out.println(
                    "USER SERVICE ERROR: "
                            + ex.getClass().getName()
            );

            System.out.println(
                    "ERROR MESSAGE: "
                            + ex.getMessage()
            );

            return getFallbackUser(userId);
        }
    }

    // Fallback for temporary User Service failures
    private UserResponse getFallbackUser(Long userId) {

        return new UserResponse(
                userId,
                "Guest User",
                "guest@example.com"
        );
    }
}