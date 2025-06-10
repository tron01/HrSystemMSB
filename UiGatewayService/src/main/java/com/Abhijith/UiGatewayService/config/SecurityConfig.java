package com.Abhijith.UiGatewayService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.Abhijith.UiGatewayService.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						                               .requestMatchers(
								                               "/",
								                               "/auth/login",
								                               "/auth/register",
								                               "/css/**",
								                               "/js/**",
								                               "/images/**",
								                               "/webjars/**"
						                               ).permitAll()
						                               .requestMatchers("/hr/**").hasRole("HR")
						                               .requestMatchers("/student/**").hasRole("USER")
						                               .anyRequest().authenticated()
				)
				.sessionManagement(session -> session
						                              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout
						                  .logoutUrl("/auth/logout")
						                  .logoutSuccessUrl("/auth/login?logout")
						                  .deleteCookies("jwt")
						                  .invalidateHttpSession(true)
						                  .clearAuthentication(true)
				);
		
		
		return http.build();
	}
}
