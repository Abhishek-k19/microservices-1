package com.example.orderservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Value("${app.message}")
    private String message;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Existing Order API
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {

        return orderService.getOrderById(id);
    }

    // Day 7 - Test centralized configuration
    @GetMapping("/config")
    public String getConfigMessage() {

        return message;
    }
}