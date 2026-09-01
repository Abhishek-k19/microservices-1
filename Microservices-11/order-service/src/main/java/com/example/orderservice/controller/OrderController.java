package com.example.orderservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderCreationService;
import com.example.orderservice.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderCreationService orderCreationService;

    @Value("${app.message}")
    private String message;

    public OrderController(
            OrderService orderService,
            OrderCreationService orderCreationService) {

        this.orderService = orderService;
        this.orderCreationService = orderCreationService;
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

    // Day 10 - Create Order with validation and transaction
    @PostMapping
    public ResponseEntity<String> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        Order order = orderCreationService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Order created successfully with ID: " + order.getId());
    }
    @GetMapping("/admin-test")
    public String adminTest() {
        return "Admin access granted";
    }
}