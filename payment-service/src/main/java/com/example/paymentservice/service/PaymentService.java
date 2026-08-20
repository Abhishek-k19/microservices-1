package com.example.paymentservice.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.paymentservice.dto.PaymentResponse;

@Service
public class PaymentService {

    private final AtomicInteger attemptCounter = new AtomicInteger(0);

    public PaymentResponse processPayment(Long orderId) {

        int attempt = attemptCounter.incrementAndGet();

        System.out.println(
                "Payment attempt " + attempt +
                " for order: " + orderId
        );

        // First attempt fails intentionally
        if (attempt == 1) {

            System.out.println("Simulating temporary payment failure");

            throw new RuntimeException("Temporary payment failure");
        }

        // Second attempt succeeds
        System.out.println("Payment successful on attempt: " + attempt);

        return new PaymentResponse(
                orderId,
                "SUCCESS",
                "Payment processed successfully on retry"
        );
    }
}