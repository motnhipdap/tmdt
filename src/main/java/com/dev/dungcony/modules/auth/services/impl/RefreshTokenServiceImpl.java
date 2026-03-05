package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.JwtConfig;
import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.TokenValidException;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.services.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    private final StringRedisTemplate redis;
    private final AccountRepository accountRepository;
    private final JwtConfig jwtConfig;

    @Override
    public String create(int userId, String deviceId) {
        long ttl = jwtConfig.getRefreshExpiration();

        // revoke token cũ của device (nếu có)
        String oldToken = redis.opsForValue()
                .get(key(userId, deviceId));

        if (oldToken != null) {
            redis.delete("refresh:" + oldToken);
        }

        String token = UUID.randomUUID().toString();

        // token -> user
        redis.opsForValue().set(
                "refresh:" + token,
                String.valueOf(userId),
                ttl,
                TimeUnit.SECONDS
        );

        // device -> token
        redis.opsForValue().set(
                key(userId, deviceId),
                token,
                ttl,
                TimeUnit.SECONDS
        );

        // user -> device
        redis.opsForSet().add(
                "user:" + userId + ":devices",
                deviceId
        );
        redis.expire(
                "user:" + userId + ":devices",
                ttl,
                TimeUnit.SECONDS
        );

        return token;
    }


    @Override
    public AccountRes verify(String refreshToken) {
        String userId = redis.opsForValue().get("refresh:" + refreshToken);

        if (userId == null)
            throw new RuntimeException("Invalid refresh token");

        Account acc = accountRepository.findById(Integer.parseInt(userId))
                .orElseThrow(TokenValidException::new);

        return new AccountRes(
                acc.getId(),
                acc.getEmail(),
                acc.getUsername(),
                acc.getPhone(),
                acc.getStatus(),
                acc.getRole(),
                acc.getCreatedAt());
    }

    @Override
    public void revoke(String refreshToken, String deviceId) {
        String userId = redis.opsForValue().get("refresh:" + refreshToken);
        if (userId == null) return;

        redis.delete("refresh:" + refreshToken);
        redis.delete(key(Integer.parseInt(userId), deviceId));
        redis.opsForSet().remove(
                "user:" + userId + ":devices",
                deviceId
        );
    }


    @Override
    public void revokeAll(int userId) {
        Set<String> devices =
                redis.opsForSet().members("user:" + userId + ":devices");

        if (devices != null) {
            for (String deviceId : devices) {
                String token = redis.opsForValue()
                        .get(key(userId, deviceId));

                if (token != null) {
                    redis.delete("refresh:" + token);
                }
                redis.delete(key(userId, deviceId));
            }
        }

        redis.delete("user:" + userId + ":devices");
    }

    private String key(int userId, String deviceId) {
        return "user:" + userId + ":device:" + deviceId + ":refresh";
    }
}


//refresh:{token}                         -> userId
//user:{userId}:devices                   -> SET(deviceId)
//user:{userId}:device:{deviceId}:refresh -> token
