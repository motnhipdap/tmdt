package com.dev.dungcony.modules.auth.dtos.res;

import com.dev.dungcony.modules.auth.enums.Role;
import com.dev.dungcony.modules.auth.enums.Status;

import java.time.Instant;

public record AccountRes(
        String email,
        String username,
        Status status,
        Role role,
        Boolean verify,
        Instant createdAt) {

    @Override
    public String toString() {
        return email + " " + username + " " + status + " " + createdAt;
    }
}
