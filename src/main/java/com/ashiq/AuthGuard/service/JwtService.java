package com.ashiq.AuthGuard.service;

import com.ashiq.AuthGuard.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(Map<String, Object> claims, Duration duration) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + duration.toMillis()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(Map<String, Object> claims) {
        return generateToken(claims, jwtProperties.getAccessTokenExpiration());
    }

    public String generateRefreshToken(Map<String, Object> claims) {
        Duration duration = jwtProperties.getRefreshTokenExpiration();
        return generateToken(claims, duration);
    }

    public String generateVerificationToken(Map<String, Object> claims) {
        Duration duration = jwtProperties.getVerificationTokenExpiration();
        return generateToken(claims, duration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmailFromToken(String jwt) {
        Claims claims = extractAllClaims(jwt);
        return claims.get("email", String.class);
    }

    public String extractUsername(String jwt) {
        Claims claims = extractAllClaims(jwt);
        return claims.getSubject();
    }
}
