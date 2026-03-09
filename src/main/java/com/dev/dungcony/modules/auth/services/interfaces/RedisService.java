package com.dev.dungcony.modules.auth.services.interfaces;

public interface RedisService {
    void cache(String key, String value);

    void delete(String key);

    String getValue(String key);
}
