package com.dev.dungcony.modules.authorization.config;

import org.springframework.beans.factory.annotation.Value;

public class Env {
    @Value("${spring.data.redis.expiration_otp}")
    public static long expiration_otp;
    @Value("${spring.data.redis.expiration_token}")
    public static long expiration_change_email;
}
