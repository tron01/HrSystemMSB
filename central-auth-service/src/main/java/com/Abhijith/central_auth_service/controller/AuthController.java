package com.Abhijith.central_auth_service.controller;

import com.Abhijith.central_auth_service.Dto.TokenValidationResponse;
import com.Abhijith.central_auth_service.Dto.UserLoginRequest;
import com.Abhijith.central_auth_service.model.User;
import com.Abhijith.central_auth_service.service.UserService;
import com.Abhijith.central_auth_service.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        
        Map<String, String> result = new HashMap<>();
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            result.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(result);
        }
        User savedUser = userService.registerUser(user);
        result.put("message", "Registration successful");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest user, HttpServletResponse response) {
       
        Map<String, String> result = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            
            String token = jwtUtil.generateToken(
                    principal.getUsername(),
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
            
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                                               .httpOnly(true)
                                               .secure(false)
                                               .sameSite("Lax") // If you are on plain HTTP (localhost dev), just switch to Lax
                                               .path("/")
                                               .maxAge(Duration.ofHours(10))
                                               .build();
            
            result.put("message", "Login successful");
            return ResponseEntity.ok()
                           .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                           .body(result);
        } catch (AuthenticationException ex) {
            result.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(result);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        
        // Clear security context first
        SecurityContextHolder.clearContext();
        ResponseCookie cleared = ResponseCookie.from("jwt", "")
                                         .httpOnly(true)
                                         .secure(false)
                                         .sameSite("Lax")
                                         .path("/")
                                         .maxAge(0)
                                         .build();
        
        Map<String, String> result = new HashMap<>();
        result.put("message", "Logout successful");
        return ResponseEntity.ok()
                       .header(HttpHeaders.SET_COOKIE, cleared.toString())
                       .body(result);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                
                if (username != null && !jwtUtil.isTokenExpired(token)) {
                    User user = userService.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                            
                    return ResponseEntity.ok(new TokenValidationResponse(username, user.getRole(), true));
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse(null, null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse(null, null, false));
        }
    }
}
