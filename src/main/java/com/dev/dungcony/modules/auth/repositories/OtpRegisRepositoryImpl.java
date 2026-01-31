package com.dev.dungcony.modules.auth.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OtpRegisRepositoryImpl implements OtpRegisRepository {

    private final RedisTemplate<String, String> redis;

    @Override
    public void cache(String key, String value) {
        redis.opsForValue().set(key, value);
    }

    @Override
    public void delete(String key) {
        redis.delete(key);
    }

    @Override
    public String getValue(String key) {
        return redis.opsForValue().get(key);
    }
}
