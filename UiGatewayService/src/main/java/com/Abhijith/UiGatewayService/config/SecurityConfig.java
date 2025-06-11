package com.Abhijith.UiGatewayService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/login", "/auth/register"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/error", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/hr/**").hasRole("HR")
                .requestMatchers("/student/**").hasRole("STUDENT")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/auth/login?logout=true")
                .deleteCookies("JSESSIONID", "remember-me")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("uniqueAndSecretKey")
                .tokenValiditySeconds(86400) // 1 day
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/auth/login?session=expired")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/auth/login?session=expired")
            );

        return http.build();
    }
}
