package com.example.orderservice.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.orderservice.dto.PaymentResponse;
import com.example.orderservice.exception.PaymentServiceUnavailableException;

import io.github.resilience4j.retry.annotation.Retry;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(
            @Value("${payment.service.url}") String paymentServiceUrl) {

        // Create HTTP request factory
        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory();

        // TIMEOUT: Order Service waits maximum 2 seconds
        requestFactory.setReadTimeout(
                Duration.ofSeconds(2)
        );

        // Create RestClient
        this.restClient = RestClient.builder()
                .baseUrl(paymentServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Retry(
        name = "paymentService",
        fallbackMethod = "paymentFallback"
    )
    public PaymentResponse getPayment(Long orderId) {

        System.out.println(
                "Calling Payment Service for order: " + orderId
        );

        return restClient
                .get()
                .uri("/api/payments/{orderId}", orderId)
                .retrieve()
                .body(PaymentResponse.class);
    }

    public PaymentResponse paymentFallback(
            Long orderId,
            Throwable throwable) {

        System.out.println(
                "All Payment Service attempts failed"
        );

        throw new PaymentServiceUnavailableException(
                "Payment Service is temporarily unavailable"
        );
    }
}