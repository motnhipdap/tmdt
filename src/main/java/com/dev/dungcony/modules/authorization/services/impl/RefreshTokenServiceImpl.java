package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.config.JwtConfig;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.exceptions.TokenValidException;
import com.dev.dungcony.modules.authorization.repositories.AccountRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final StringRedisTemplate redis;
    private final AccountRepository accountRepository;

    @Override
    public String create(int id) {

        String refreshToken = UUID.randomUUID().toString();
        redis.opsForValue()
                .set(
                        key(refreshToken),
                        String.valueOf(id),
                        JwtConfig.refreshExpiration,
                        TimeUnit.SECONDS
                );

        return refreshToken;
    }

    @Override
    public AccountRes verify(String refreshToken) {
        String userId = redis.opsForValue().get(key(refreshToken));

        if (userId == null) {
            throw new RuntimeException("Invalid refresh token");
        }
        Account acc = accountRepository.findById(Integer.valueOf(userId)).orElseThrow(TokenValidException::new);

        return new AccountRes(
                acc.getId(),
                acc.getEmail(),
                acc.getUsername(),
                acc.getPhone(),
                acc.getStatus(),
                acc.getRole(),
                acc.getCreatedAt()
        );
    }

    @Override
    public void revoke(String refreshToken) {

    }

    @Override
    public void revokeAll(Account account) {

    }

    private String key(String token) {
        return "refresh:" + token;
    }
}
