package com.dev.dungcony.modules.authorization.services.interfaces;

public interface JwtService {
    String generateToken(int id, String username, String role);
}
