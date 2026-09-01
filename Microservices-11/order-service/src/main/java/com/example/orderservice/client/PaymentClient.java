package com.example.orderservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.orderservice.dto.PaymentResponse;

@Component
public class PaymentClient {

    private final RestClient paymentRestClient;

    public PaymentClient(
            @org.springframework.beans.factory.annotation.Qualifier("paymentRestClient")
            RestClient paymentRestClient) {

        this.paymentRestClient = paymentRestClient;
    }

    public PaymentResponse getPayment(Long orderId) {

        return paymentRestClient
                .get()
                .uri("/payments/{orderId}", orderId)
                .retrieve()
                .body(PaymentResponse.class);
    }
}