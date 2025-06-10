package com.Abhijith.UiGatewayService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private String username;
    private String role;
    private boolean valid;
}
