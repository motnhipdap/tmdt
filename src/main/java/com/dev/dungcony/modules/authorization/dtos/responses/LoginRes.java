package com.dev.dungcony.modules.authorization.dtos.responses;

import com.dev.dungcony.modules.authorization.config.JwtConfig;

public record LoginRes(
        String token,
        String header,
        long expiration) {
    public LoginRes(String token, long expiration) {
        this(token, JwtConfig.headerPrefix, expiration);
    }
}
