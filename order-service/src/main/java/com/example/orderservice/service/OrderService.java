package com.example.orderservice.service;

import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.PaymentResponse;
import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.exception.OrderNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final UserClient userClient;
    private final PaymentClient paymentClient;

    public OrderService(
            UserClient userClient,
            PaymentClient paymentClient) {

        this.userClient = userClient;
        this.paymentClient = paymentClient;
    }

    public OrderResponse getOrderById(Long orderId) {

        Long userId;
        String productName;

        if (orderId == 101L) {

            userId = 1L;
            productName = "Laptop";

        } else if (orderId == 102L) {

            userId = 2L;
            productName = "Mobile";

        } else {

            throw new OrderNotFoundException(
                    "Order not found with id: " + orderId
            );
        }

        // Call User Service
        UserResponse user =
                userClient.getUserById(userId);

        // Call Payment Service
        PaymentResponse payment =
                paymentClient.getPayment(orderId);

        // Combine everything into one response
        return new OrderResponse(
                orderId,
                userId,
                productName,
                user,
                payment
        );
    }
}