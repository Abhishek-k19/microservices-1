package com.example.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.UserResponse;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final UserClient userClient;

    public OrderController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable Long orderId) {

        UserResponse user = userClient.getUserById(1L);

        return "Order " + orderId
                + " belongs to " + user.name();
    }
}