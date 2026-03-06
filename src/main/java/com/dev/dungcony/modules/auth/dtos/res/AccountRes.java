package com.dev.dungcony.modules.auth.dtos.res;

import com.dev.dungcony.modules.auth.enums.Status;

import java.time.Instant;

public record AccountRes(
        int id,
        String email,
        String username,
        Status status,
        String role,
        Instant createdAt) {

    @Override
    public String toString() {
        return email + " " + username + " " + status + " " + createdAt;
    }
}
