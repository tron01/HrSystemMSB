package com.Abhijith.UiGatewayService.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('USER')")
public class StudentDashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add any data needed for the student dashboard
        return "student/dashboard";
    }
}
