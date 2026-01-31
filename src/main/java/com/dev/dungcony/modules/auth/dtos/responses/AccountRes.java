package com.dev.dungcony.modules.auth.dtos.responses;

import java.time.LocalDateTime;

public record AccountRes(
        int id,
        String email,
        String username,
        String phone,
        String status,
        String role,
        LocalDateTime create_at
) {

    @Override
    public String toString() {
        return email + " " + username + " " + phone + " " + status + " " + create_at;
    }
}
