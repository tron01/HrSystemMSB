package com.Abhijith.central_auth_service.filter;

import com.Abhijith.central_auth_service.service.CustomUserDetailsService;
import com.Abhijith.central_auth_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwt = resolveToken(request);        //  Authorization header in one place
        
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String username = jwtUtil.extractUsername(jwt);   // may throw → handled below
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    // Prefer method that checks full UserDetails, if you have it
                    if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                        
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.info("------------------------------------------------------");
                        log.info("User authenticated - Username: {}, Role: {}", userDetails.getUsername()
                                ,userDetails.getAuthorities());
                        log.info("Request URI: {}", request.getRequestURI());
                        log.info("-----------------------------------------------------");
                    } else {
                        log.warn("JWT validation failed for user {}", username);
                    }
                }
            } catch (Exception ex) {   // ExpiredJwtException, MalformedJwtException, etc.
                log.warn("JWT processing error: {}", ex.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
}

    /** Extract JWT from HttpOnly cookie first, then “Authorization: Bearer …” header. */
    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("jwt".equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                    return c.getValue();
                }
            }
        }
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}
