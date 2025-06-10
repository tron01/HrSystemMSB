package com.Abhijith.central_auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    // In production, use a longer, more secure key and store it in environment variables
    private final String SECRET_KEY = "sdsgsggsawgwgqkjgkajgnkjwgnkjwngkjnkjngkjakjkwfwgkjawgjbg";
    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    // Create key just once for better performance
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username,String role) {
        return Jwts.builder()
                       .claim("sub", username)
                       .claim("role", role)
                       .claim("iat", new Date(System.currentTimeMillis()))
                       .claim("exp", new Date(System.currentTimeMillis() + EXPIRATION))
                       .issuedAt(new Date(System.currentTimeMillis()))
                       .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                       .signWith(key)
                       .compact();
                
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}


