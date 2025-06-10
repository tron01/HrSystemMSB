package com.Abhijith.UiGatewayService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserloginRequest {
private String username;
private String password;
}
