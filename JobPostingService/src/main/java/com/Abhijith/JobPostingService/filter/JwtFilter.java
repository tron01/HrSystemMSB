package com.Abhijith.JobPostingService.filter;


import com.Abhijith.JobPostingService.util.JwtUtil;
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
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwt;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req,
	                                HttpServletResponse res,
	                                FilterChain chain)
			throws ServletException, IOException {
		
		String token = resolve(req);
		if (token != null && jwt.valid(token)
				    && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			var auth = new UsernamePasswordAuthenticationToken(
					jwt.username(token),
					token,                                             // ✅ store token
					jwt.roles(token).stream()
							.map(SimpleGrantedAuthority::new)
							.collect(Collectors.toList())
			);
			auth.setDetails(new WebAuthenticationDetailsSource()
					                .buildDetails(req));
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(req, res);
	}
	
	private String resolve(HttpServletRequest r) {
		// 1️⃣ Http-only cookie
		if (r.getCookies() != null)
			for (Cookie c : r.getCookies())
				if ("jwt".equals(c.getName()) && !c.getValue().isBlank())
					return c.getValue();
		// 2️⃣ Bearer header
		var h = r.getHeader("Authorization");
		return h != null && h.startsWith("Bearer ") ? h.substring(7) : null;
	}
}

