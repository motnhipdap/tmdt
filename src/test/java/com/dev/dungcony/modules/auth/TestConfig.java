package com.dev.dungcony.modules.auth;

import com.dev.dungcony.modules.auth.config.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test configuration for authorization module unit tests
 */
@TestConfiguration
public class TestConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JwtConfig jwtConfig() {
        JwtConfig config = new JwtConfig();
        // Secret key must be >= 512 bits (64 bytes) for HS512 algorithm
        config.setSecret("mySecretKeyForJWTTokenGenerationAndValidation1234567890123456789012345678901234567890");
        config.setExpiration(3600000L); // 1 hour
        config.setRefreshExpiration(7200000L); // 2 hours
        return config;
    }

    // @Bean
    // public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws
    // Exception {
    // http
    // .csrf(AbstractHttpConfigurer::disable)
    // .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    // return http.build();
    // }
}
