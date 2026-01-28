package com.dev.dungcony.modules.authorization.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordReq(
                @NotBlank(message = "old password not blank") String oldPass,
                @NotBlank(message = "new password not blank") String newPass) {
}
