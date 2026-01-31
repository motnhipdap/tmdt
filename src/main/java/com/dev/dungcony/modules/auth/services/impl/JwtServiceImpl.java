package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.JwtConfig;
import com.dev.dungcony.modules.auth.services.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final Key key;
    private final JwtConfig jwtConfig;

    public JwtServiceImpl(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    @Override
    public String generateToken(int id, String username, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getExpiration() * 1000);

        logger.info(now.getTime() + " " + expiration.getTime() + " " + username + " " + role);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512) // HS512 cho HMAC-SHA512
                .compact();
    }

    @Override
    public String generateToken(int id, String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512) // HS512 cho HMAC-SHA512
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Integer extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            // Subject chứa user ID
            String subject = claims.getSubject();
            return Integer.parseInt(subject);
        } catch (Exception e) {
            logger.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String extractRole(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            logger.error("Error extracting role from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String extractEmail(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("email", String.class);
        } catch (Exception e) {
            logger.error("Error extracting email from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
