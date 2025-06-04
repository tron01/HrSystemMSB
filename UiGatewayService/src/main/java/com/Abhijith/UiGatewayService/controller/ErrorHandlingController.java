package com.Abhijith.UiGatewayService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorHandlingController {
	
	@GetMapping("/error")
	public String handleError() {
		// This method will be called when an error occurs
		// You can return a custom error page or a specific view name
		return "error"; // Assuming you have an error.html template in your resources/templates directory
	}
	
}
