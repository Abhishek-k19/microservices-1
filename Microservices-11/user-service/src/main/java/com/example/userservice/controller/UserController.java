package com.example.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.UserResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id,
            @RequestHeader(
                    value = "X-Correlation-ID",
                    required = false
            ) String correlationId)
            throws InterruptedException {

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = "NO-CORRELATION-ID";
        }

        System.out.println(
                "USER SERVICE - Correlation ID: "
                        + correlationId
        );

        Thread.sleep(1000);

        // Valid users
        if (id == 1L) {
            return ResponseEntity.ok(
                    new UserResponse(
                            1L,
                            "John",
                            "john@example.com"
                    )
            );
        }

        if (id == 2L) {
            return ResponseEntity.ok(
                    new UserResponse(
                            2L,
                            "Jane",
                            "jane@example.com"
                    )
            );
        }

        // User does not exist
        return ResponseEntity.notFound().build();
    }
}