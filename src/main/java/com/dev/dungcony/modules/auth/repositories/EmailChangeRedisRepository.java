package com.dev.dungcony.modules.auth.repositories;

import java.util.Map;

public interface EmailChangeRedisRepository {
    void createRequest(
            int userId,
            String oldEmail,
            String oldOtpHash,
            long ttlSeconds);

    boolean exists(int userId);

    Map<String, String> getAll(int userId);

    String getField(int userId, String field);

    void setField(int userId, String field, String value);

    void delete(int userId);
}
