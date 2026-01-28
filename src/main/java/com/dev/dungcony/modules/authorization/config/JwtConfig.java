package com.dev.dungcony.modules.authorization.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    public static String secret;
    @Value("${jwt.expiration}")
    public static Long expiration;
    @Value("${jwt.refresh-expiration}")
    public static Long refreshExpiration;

    public static final String headerPrefix = "Bearer ";
}
