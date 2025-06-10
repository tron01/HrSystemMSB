package com.Abhijith.UiGatewayService.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_HR"))) {
                return "redirect:/hr/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                return "redirect:/student/dashboard";
            }
        }
        return "home";
    }
}
