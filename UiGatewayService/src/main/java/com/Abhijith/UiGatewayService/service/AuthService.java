package com.Abhijith.UiGatewayService.service;

import com.Abhijith.UiGatewayService.client.AuthServiceClient;
import com.Abhijith.UiGatewayService.dto.UserloginRequest;
import com.Abhijith.UiGatewayService.model.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import feign.FeignException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final AuthServiceClient authServiceClient;

    public AuthService(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    public ResponseEntity<?> login(UserloginRequest request) {
        try {
            return authServiceClient.loginUser(request);
        } catch (FeignException.Unauthorized ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(error);
        }
    }

    public ResponseEntity<?> register(UserDto userDto) {
        try {
            return authServiceClient.registerUser(userDto);
        } catch (FeignException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed.");
            return ResponseEntity.status(400).body(error);
        }
    }

    public ResponseEntity<?> logout() {
        try {
            return authServiceClient.logoutUser();
        } catch (FeignException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Logout failed.");
            return ResponseEntity.status(400).body(error);
        }
    }
}