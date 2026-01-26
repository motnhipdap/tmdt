package com.dev.dungcony.modules.authorization.dtos.requests;

import jakarta.validation.constraints.Pattern;

public record LoginReq(
        String username,

        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username chỉ gồm chữ, số và dấu _")
        String password
) {
}

