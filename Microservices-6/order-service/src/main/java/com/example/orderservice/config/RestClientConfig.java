package com.example.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient userRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Bean
    RestClient paymentRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }
}