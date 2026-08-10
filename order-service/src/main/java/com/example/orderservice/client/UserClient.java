package com.example.orderservice.client;

import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.exception.UserServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(
            @Value("${user.service.url}") String userServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public UserResponse getUserById(Long userId) {

        try {
            return restClient
                    .get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .body(UserResponse.class);

        } catch (RestClientException ex) {

            throw new UserServiceUnavailableException(
                    "User Service is currently unavailable");
        }
    }
}