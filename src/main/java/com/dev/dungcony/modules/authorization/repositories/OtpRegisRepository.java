package com.dev.dungcony.modules.authorization.repositories;

public interface OtpRegisRepository {
    void cache(String key, String value);

    void delete(String key);

    String getValue(String key);

}
