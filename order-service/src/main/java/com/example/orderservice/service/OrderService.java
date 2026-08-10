package com.example.orderservice.service;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final UserClient userClient;

    public OrderService(UserClient userClient) {
        this.userClient = userClient;
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
            return null;
        }

        UserResponse user = userClient.getUserById(userId);

        return new OrderResponse(
                orderId,
                userId,
                productName,
                user
        );
    }
}