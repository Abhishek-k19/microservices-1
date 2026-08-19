package com.example.orderservice.dto;

public class OrderResponse {

    private Long orderId;
    private Long userId;
    private String productName;
    private UserResponse user;
    private PaymentResponse payment;

    public OrderResponse() {
    }

    public OrderResponse(
            Long orderId,
            Long userId,
            String productName,
            UserResponse user,
            PaymentResponse payment) {

        this.orderId = orderId;
        this.userId = userId;
        this.productName = productName;
        this.user = user;
        this.payment = payment;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }
}