package com.Abhijith.central_auth_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserLoginRequest {

private String username;
private String password;

}
