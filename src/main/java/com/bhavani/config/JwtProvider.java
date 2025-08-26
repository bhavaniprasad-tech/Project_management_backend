package com.bhavani.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static SecretKey key;

    // Inject secret from application.properties
    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        // Initialize the key AFTER the secret is injected
        // Make sure your secret is at least 32 characters long
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate JWT token with username as subject and email claim (optional)
    public static String generateToken(Authentication auth) {
        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("email", auth.getName()) // add email claim (optional)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +  1000L * 60 * 60 * 24 * 7)) // 7 day expiry
                .signWith(key)
                .compact();
    }

    // Extract username from token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Extract email claim (if you added it)
    public String getEmailFromToken(String token) {

        token = token.substring(7);

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class);
    }

    // Validate token by checking username matches UserDetails
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername());
    }
}
