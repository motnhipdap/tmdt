package com.dev.dungcony.modules.auth.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class EmailChangeRedisRepositoryImpl implements EmailChangeRedisRepository {
    private final RedisTemplate<String, String> redis;

    private String key(int userId) {
        return "email:change:" + userId;
    }

    @Override
    public boolean exists(int userId) {
        return redis.hasKey(key(userId));
    }

    @Override
    public Map<String, String> getAll(int userId) {
        Map<Object, Object> entries = redis.opsForHash().entries(key(userId));
        return entries.entrySet().stream()
                .filter(e -> e.getKey() instanceof String && e.getValue() instanceof String)
                .collect(java.util.stream.Collectors.toMap(
                        e -> (String) e.getKey(),
                        e -> (String) e.getValue()));
    }

    @Override
    public String getField(int userId, String field) {
        return (String) redis.opsForHash().get(key(userId), field);
    }

    @Override
    public void setField(int userId, String field, String value) {
        redis.opsForHash().put(key(userId), field, value);
    }

    @Override
    public void delete(int userId) {
        redis.delete(key(userId));
    }

    @Override
    public void createRequest(
            int userId,
            String oldEmail,
            String oldOtpHash,
            long ttl) {

        String key = key(userId);

        redis.opsForHash().put(key, "step", "VERIFY_OLD_EMAIL");
        redis.opsForHash().put(key, "old_email", oldEmail);
        redis.opsForHash().put(key, "old_otp_hash", oldOtpHash);
        redis.opsForHash().put(key, "old_verified", "false");

        redis.expire(key, ttl, TimeUnit.SECONDS);
    }

}
