package com.dev.dungcony.commons.dtos;

import java.time.Instant;

public record ApiErrorLogEntry(
        Instant timestamp,
        String method,
        String path,
        int status,
        String error,
        String message) {
    public ApiErrorLogEntry(String method, String path, int status, String error, String message) {
        this(Instant.now(), method, path, status, error, message);
    }
}
