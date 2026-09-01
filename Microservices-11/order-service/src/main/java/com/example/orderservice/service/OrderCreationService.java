package com.example.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.repository.OrderRepository;

@Service
public class OrderCreationService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;

    public OrderCreationService(
            OrderRepository orderRepository,
            UserClient userClient) {

        this.orderRepository = orderRepository;
        this.userClient = userClient;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        // Verify User before creating the Order
        UserResponse user =
                userClient.getUserById(request.getUserId());

        // Create Order
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("CREATED");

        // Create Order Items
        for (OrderItemRequest itemRequest : request.getItems()) {

            OrderItem item = new OrderItem();
            item.setProductId(itemRequest.getProductId());
            item.setQuantity(itemRequest.getQuantity());

            order.addItem(item);
        }

        // Save only after user verification
        return orderRepository.save(order);
    }
}