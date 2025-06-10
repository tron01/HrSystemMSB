package com.Abhijith.UiGatewayService.service;

import com.Abhijith.UiGatewayService.client.AuthServiceClient;
import com.Abhijith.UiGatewayService.dto.UserloginRequest;
import com.Abhijith.UiGatewayService.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthServiceClient authServiceClient;
    
    public ResponseEntity<?> register(UserDto userDto) {
        return authServiceClient.registerUser(userDto);
    }
    
    public ResponseEntity<?> login(UserloginRequest userDto) {
        return authServiceClient.loginUser(userDto);
    }
    
    public ResponseEntity<?> logout() {
        return authServiceClient.logoutUser();
    }
}
