package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.services.interfaces.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
@Service
public class RedisImpl implements RedisService {
    private final RedisTemplate<String, String> redis;

    @Override
    public void cache(String key, String value) {
        redis.opsForValue().set(key, value);
    }

    @Override
    public void cache(String key, String value, long ttl) {
        redis.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
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