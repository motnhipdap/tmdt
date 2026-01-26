package com.dev.dungcony.modules.authorization.dtos.responses;

import java.time.LocalDateTime;

public record AccountDetail(
        String email,
        String username,
        String phone,
        String status,
        LocalDateTime create_at
) {

    @Override
    public String toString() {
        return email + " " + username + " " + phone + " " + status + " " + create_at;
    }
}
