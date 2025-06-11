package com.Abhijith.JobPostingService.util;


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

private static final String SECRET =
		"sdsgsggsawgwgqkjgkajgnkjwgnkjwngkjnkjngkjakjkwfwgkjawgjbg"; // ✅ inject in prod
private final SecretKey key =
		Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

private Claims parse(String token) {
	return Jwts.parser().verifyWith(key).build()
			       .parseSignedClaims(token).getPayload();
}

public <T> T extract(String token, Function<Claims, T> f) {
	return f.apply(parse(token));
}
public String username(String t)            { return extract(t, Claims::getSubject); }
public List<String> roles(String t)         { return extract(t, c -> c.get("roles", List.class)); }
public Date expiry(String t)                { return extract(t, Claims::getExpiration); }

public boolean valid(String t) {
	try { return expiry(t).after(new Date()); }
	catch (JwtException | IllegalArgumentException e) { return false; }
}
}
