package com.Abhijith.UiGatewayService.filter;

import com.Abhijith.UiGatewayService.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        
        String token = resolveToken(request);
        
        if (token != null && jwtUtil.validate(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            String username = jwtUtil.extractUsername(token);
            List<SimpleGrantedAuthority> authorities =
                    jwtUtil.extractRoles(token).stream()
                            .map(SimpleGrantedAuthority::new)   // ROLE_HR, ROLE_USER, …
                            .collect(Collectors.toList());
            
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            log.debug("Authenticated '{}' with roles {}", username, authorities);
        }
        
        chain.doFilter(request, response);
    }
    
    /* ------------ helpers ------------ */
    private String resolveToken(HttpServletRequest req){
        // 1️⃣ HttpOnly cookie
        if (req.getCookies()!=null){
            for (Cookie c: req.getCookies()){
                if ("jwt".equals(c.getName()) && !c.getValue().isBlank()){
                    return c.getValue();
                }
            }
        }
        // 2️⃣ Authorization header
        String hdr = req.getHeader("Authorization");
        return (hdr!=null && hdr.startsWith("Bearer ")) ? hdr.substring(7) : null;
    }
}