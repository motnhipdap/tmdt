package com.dev.dungcony.modules.auth.dtos.responses;

import com.dev.dungcony.modules.auth.config.JwtConfig;

public record LoginRes(
        String token,
        String header,
        long expiration) {
    public LoginRes(String token, long expiration) {
        this(token, JwtConfig.headerPrefix, expiration);
    }
}
