package com.dev.dungcony.modules.auth.dtos.responses;

import java.time.Instant;

public record AccountRes(
        int id,
        String email,
        String username,
        String phone,
        String status,
        String role,
        Instant createdAt
) {

    @Override
    public String toString() {
        return email + " " + username + " " + phone + " " + status + " " + createdAt;
    }
}
