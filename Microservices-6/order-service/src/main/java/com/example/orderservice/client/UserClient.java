package com.example.orderservice.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.orderservice.dto.UserResponse;

@Component
public class UserClient {

    private final RestClient userRestClient;

    public UserClient(
            @Qualifier("userRestClient")
            RestClient userRestClient) {

        this.userRestClient = userRestClient;
    }

    public UserResponse getUserById(Long userId) {

        return userRestClient
                .get()
                .uri("/users/{id}", userId)
                .retrieve()
                .body(UserResponse.class);
    }
}