package com.dev.dungcony.modules.auth.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(
        @NotBlank(message = "username not blank") String username,
        @NotBlank(message = "password not blank") String password) {
}
