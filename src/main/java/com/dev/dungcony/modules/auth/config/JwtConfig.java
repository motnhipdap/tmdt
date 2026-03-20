package com.dev.dungcony.modules.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${jwt.refresh-cookie.secure:false}")
    private boolean refreshCookieSecure;

    @Value("${jwt.refresh-cookie.path:/v1/api/public/auth}")
    private String refreshCookiePath;

    public static final String headerPrefix = "Bearer ";
}
