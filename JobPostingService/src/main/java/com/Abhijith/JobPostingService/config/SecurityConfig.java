package com.Abhijith.JobPostingService.config;


import com.Abhijith.JobPostingService.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtFilter jwtFilter;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				       .csrf(cs -> cs.disable())
				       .sessionManagement(sm ->
						                          sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				       .authorizeHttpRequests(auth -> auth
						                                      .requestMatchers("/actuator/**").permitAll()
						                                      .requestMatchers("/jobs/**").hasAnyRole("HR", "ADMIN")
						                                      .anyRequest().authenticated())
				       .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				       .build();
	}
}
