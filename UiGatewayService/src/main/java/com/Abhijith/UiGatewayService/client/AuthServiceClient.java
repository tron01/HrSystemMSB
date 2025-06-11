package com.Abhijith.UiGatewayService.client;

import com.Abhijith.UiGatewayService.dto.UserloginRequest;
import com.Abhijith.UiGatewayService.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "central-auth-service", path = "/api/auth")
public interface AuthServiceClient {
    
    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody UserDto userDto);
    
    @PostMapping("/login")
    ResponseEntity<?> loginUser(@RequestBody UserloginRequest userDto);
    
    @PostMapping("/logout")
    ResponseEntity<?> logoutUser();
    
}
