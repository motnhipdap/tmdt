package com.dev.dungcony.modules.auth.repositories;

public interface OtpRegisRepository {
    void cache(String key, String value);

    void delete(String key);

    String getValue(String key);

}
