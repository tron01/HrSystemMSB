package com.Abhijith.UiGatewayService.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

	// 👇  use exactly the same value as central-auth-service (ideally inject from env/Config Server)
	private static final String SECRET = "sdsgsggsawgwgqkjgkajgnkjwgnkjwngkjnkjngkjakjkwfwgkjawgjbg";
	private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	
	
	/* ---------- claim helpers ---------- */
	public <T> T extractClaim(String token, Function<Claims,T> resolver) {
		return resolver.apply(parse(token));
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token){
		return extractClaim(token, Claims::getExpiration);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> extractRoles(String token)     {
		return extractClaim(token, c -> c.get("roles", List.class));
	}
	
	/* ---------- validation ---------- */
	public boolean isExpired(String token){ return extractExpiration(token).before(new Date()); }
	public boolean validate(String token){
		try { parse(token);
			return !isExpired(token);
		}
		catch (JwtException | IllegalArgumentException ex){
			return false;
		}
	}
	
	/* ---------- internal ---------- */
	private Claims parse(String token){
		return Jwts.parser()
				       .verifyWith(key)
				       .build()
				       .parseSignedClaims(token)
				       .getPayload();
	}
}
