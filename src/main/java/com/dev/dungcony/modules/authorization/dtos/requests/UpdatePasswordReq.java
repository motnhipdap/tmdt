package com.dev.dungcony.modules.authorization.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordReq(String oldPass, @NotBlank @Valid() String newPass) {
}
