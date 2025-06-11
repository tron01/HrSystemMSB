package com.Abhijith.UiGatewayService.controller;

import com.Abhijith.UiGatewayService.dto.UserloginRequest;
import com.Abhijith.UiGatewayService.model.UserDto;
import com.Abhijith.UiGatewayService.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
			String setCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
			if (setCookie != null) {
				response.addHeader(HttpHeaders.SET_COOKIE, setCookie);
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
	
	@PostMapping("/logout")
	public String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		ResponseEntity<?> logoutResponse = authService.logout();
		
		// Clear JWT cookie in client
		Cookie cookie = new Cookie("jwt", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		response.addCookie(cookie);
		
		if (logoutResponse.getStatusCode().is2xxSuccessful()) {
			redirectAttributes.addFlashAttribute("message", "You have been logged out.");
		} else {
			redirectAttributes.addFlashAttribute("error", "Logout failed.");
		}
		
		return "redirect:/auth/login";
	}
}
