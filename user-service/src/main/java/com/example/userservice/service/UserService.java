package com.example.userservice.service;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, UserResponse> users = new HashMap<>();

    public UserService() {

        users.put(1L,new UserResponse(1L,"Ramu","ramu@gmail.com"));

        users.put(2L,new UserResponse(2L,"Abhishek","abhishek@gmail.com"));
    }

    public UserResponse getUserById(Long id) {

        UserResponse user = users.get(id);

        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        return user;
    }
}