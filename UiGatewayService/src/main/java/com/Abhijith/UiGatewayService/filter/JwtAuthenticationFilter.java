package com.Abhijith.UiGatewayService.filter;

import com.Abhijith.UiGatewayService.client.AuthServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Skip authentication for permitted URLs
        if (isPermittedUrl(request.getRequestURI())) {
            log.debug("Skipping authentication for permitted URL: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = extractJwtFromCookies(request);
        
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Call central-auth-service to validate token
                var validationResponse = authServiceClient.validateToken("Bearer " + jwt);
                
                if (validationResponse.getStatusCode().is2xxSuccessful() &&
                    validationResponse.getBody() != null &&
                    validationResponse.getBody().isValid()) {
                    
                    var userDetails = validationResponse.getBody();
                    var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + userDetails.getRole()))
                    );
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("------------------------------------------------------");
                    log.info("User authenticated - Username: {}, Role: {}", userDetails.getUsername(),userDetails.getRole());
                    log.info("Request URI: {}", request.getRequestURI());
                    log.info("-----------------------------------------------------");
                } else {
                    log.warn("Token validation failed for request to: {}", request.getRequestURI());
                }
            } catch (Exception e) {
                log.error("Could not validate JWT token for request to: {}",
                           request.getRequestURI(), e);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        }
        return null;
    }

    private boolean isPermittedUrl(String uri) {
        return uri.startsWith("/auth/login") ||
               uri.startsWith("/auth/register") ||
               uri.equals("/") ||
               uri.startsWith("/css/") ||
               uri.startsWith("/js/") ||
               uri.startsWith("/images/");
    }
}
