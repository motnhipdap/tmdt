package com.dev.dungcony.modules.authorization.services.interfaces;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateToken(int id, String username, String role);

    String extractUsername(String token);

    Integer extractUserId(String token);

    String extractRole(String token);

    boolean validateToken(String token);

    boolean isTokenExpired(String token);

    Claims extractAllClaims(String token);
}
