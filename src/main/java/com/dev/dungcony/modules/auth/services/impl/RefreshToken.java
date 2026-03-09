//package com.dev.dungcony.modules.auth.services.impl;
//
//import com.dev.dungcony.modules.auth.config.JwtConfig;
//import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
//import com.dev.dungcony.modules.auth.entities.Account;
//import com.dev.dungcony.modules.auth.exceptions.TokenValidException;
//import com.dev.dungcony.modules.auth.repositories.AccountRepository;
//import com.dev.dungcony.modules.auth.services.interfaces.RefreshTokenService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class RefreshTokenImpl implements RefreshTokenService {
//
//    private final StringRedisTemplate redis;
//    private final AccountRepository accountRepository;
//    private final JwtConfig jwtConfig;
//
//    @Override
//    public String create(int accId, String deviceId) {
//        long ttl = jwtConfig.getRefreshExpiration();
//
//        // revoke token cũ của device (nếu có)
//        String oldToken = redis.opsForValue()
//                .get(key(accId, deviceId));
//
//        if (oldToken != null) {
//            redis.delete("refresh:" + oldToken);
//        }
//
//        String token = UUID.randomUUID().toString();
//
/// /         token -> user
//        redis.opsForValue().set(
//                "refresh:" + token,
//                String.valueOf(accId),
//                ttl,
//                TimeUnit.SECONDS);
//
//        // device -> token
//        redis.opsForValue().set(
//                key(accId, deviceId),
//                token,
//                ttl,
//                TimeUnit.SECONDS);
//
//        // user -> device
//        redis.opsForSet().add(
//                "user:" + accId + ":devices",
//                deviceId);
//        redis.expire(
//                "user:" + accId + ":devices",
//                ttl,
//                TimeUnit.SECONDS);
//
//        return token;
//    }
//
//    @Override
//    public AccountRes verify(String refreshToken) {
//        String accId = redis.opsForValue().get("refresh:" + refreshToken);
//
//        if (accId == null)
//            throw new TokenValidException();
//
//        Account acc = accountRepository.findById(Integer.parseInt(accId))
//                .orElseThrow(TokenValidException::new);
//
//        return new AccountRes(
//                acc.getEmail(),
//                acc.getUsername(),
//                acc.getStatus(),
//                acc.getRole(),
//                acc.getVerify(),
//                acc.getCreatedAt());
//    }
//
//    @Override
//    public void revoke(String refreshToken, String deviceId) {
//        String accId = redis.opsForValue().get("refresh:" + refreshToken);
//        if (accId == null)
//            return;
//
//        redis.delete("refresh:" + refreshToken);
//        redis.delete(key(Integer.parseInt(accId), deviceId));
//        redis.opsForSet().remove(
//                "user:" + accId + ":devices",
//                deviceId);
//    }
//
//    @Override
//    public void revokeAll(int accId) {
//        Set<String> devices = redis.opsForSet().members("user:" + accId + ":devices");
//
//        if (devices != null) {
//            for (String deviceId : devices) {
//                String token = redis.opsForValue()
//                        .get(key(accId, deviceId));
//
//                if (token != null) {
//                    redis.delete("refresh:" + token);
//                }
//                redis.delete(key(accId, deviceId));
//            }
//        }
//
//        redis.delete("user:" + accId + ":devices");
//    }
//
//    private String key(int accId, String deviceId) {
//        return "user:" + accId + ":device:" + deviceId + ":refresh";
//    }
//}
//
//// refresh:{token} -> accId
//// user:{accId}:devices -> SET(deviceId)
//// user:{accId}:device:{deviceId}:refresh -> token
