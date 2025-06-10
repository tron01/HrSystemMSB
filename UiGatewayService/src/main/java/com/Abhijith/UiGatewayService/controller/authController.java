package com.Abhijith.UiGatewayService.controller;

import com.Abhijith.UiGatewayService.dto.UserloginRequest;
import com.Abhijith.UiGatewayService.model.UserDto;
import com.Abhijith.UiGatewayService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("userDto", new UserDto());
		return "login";
	}

@PostMapping("/login")
public String login(@ModelAttribute UserloginRequest userDto,
                    HttpServletResponse response,
                    RedirectAttributes redirectAttributes) {
	
	ResponseEntity<?> loginResponse = authService.login(userDto);
	
	if (loginResponse.getStatusCode().is2xxSuccessful()) {
		
		// ➜ Grab the Set-Cookie header coming from central-auth-service
		String setCookie = loginResponse.getHeaders()
				                   .getFirst(HttpHeaders.SET_COOKIE);
		
		if (setCookie != null) {
			// ➜ Forward it to the real client
			response.addHeader(HttpHeaders.SET_COOKIE, setCookie);
		} else {
			// Fallback: create your own cookie if you prefer
		}
		
		return "redirect:/";
	}
	
	redirectAttributes.addFlashAttribute("error", "Invalid username or password");
	return "redirect:/auth/login";
}



@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("userDto", new UserDto());
		return "register";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
		ResponseEntity<?> response = authService.register(userDto);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			redirectAttributes.addFlashAttribute("message", "Registration successful. Please login.");
			return "redirect:/auth/login";
		}
		
		redirectAttributes.addFlashAttribute("error", "Registration failed.");
		return "redirect:/auth/register";
	}

	@GetMapping("/logout")
	public String logout(RedirectAttributes redirectAttributes) {
		ResponseEntity<?> response = authService.logout();
		
		if (response.getStatusCode().is2xxSuccessful()) {
			redirectAttributes.addFlashAttribute("message", "You have been logged out.");
		} else {
			redirectAttributes.addFlashAttribute("error", "Logout failed. Try again.");
		}
		
		return "redirect:/auth/login";
	}
	
	
}
