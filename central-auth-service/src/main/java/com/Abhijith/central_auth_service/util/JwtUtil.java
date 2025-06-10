package com.Abhijith.central_auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "sdsgsggsawgwgqkjgkajgnkjwgnkjwngkjnkjngkjakjkwfwgkjawgjbg"; // should ideally come from env
    private static final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours
    
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                       .verifyWith(key)
                       .build()
                       .parseSignedClaims(token)
                       .getPayload();
    }
    
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                       .subject(username)
                       .claim("roles", roles)
                       .issuedAt(new Date())
                       .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                       .signWith(key)
                       .compact();
    }
    
    public boolean validateToken(String token, String expectedUsername) {
        try {
            final String actualUsername = extractUsername(token);
            return actualUsername.equals(expectedUsername) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false; // invalid token
        }
    }
}
