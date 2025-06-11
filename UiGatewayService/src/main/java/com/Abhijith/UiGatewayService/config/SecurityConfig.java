package com.Abhijith.UiGatewayService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.Abhijith.UiGatewayService.filter.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtFilter){
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				       .csrf(AbstractHttpConfigurer::disable)
				       .authorizeHttpRequests(auth -> auth
						                                      .requestMatchers("/auth/login", "/auth/register", "/css/**", "/", "/js/**").permitAll()
						                                      .requestMatchers("/hr/**").hasRole("HR")
						                                      .requestMatchers("/student/**").hasRole("USER")
						                                      .anyRequest().authenticated()
				       )
				       .logout(logout -> logout
						                         .logoutUrl("/auth/logout")
						                         .logoutSuccessUrl("/auth/login?logout")
						                         .deleteCookies("jwt")
						                         .invalidateHttpSession(true)
						                         .clearAuthentication(true)
				       )
				       .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				       .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				       .build();
	}
	
	
}