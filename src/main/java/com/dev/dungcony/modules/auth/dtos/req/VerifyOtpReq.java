package com.dev.dungcony.modules.auth.dtos.req;

import com.dev.dungcony.modules.auth.dtos.OtpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyOtpReq(
        @NotBlank(message = "email not blank") @Email(message = "incorrect format") String email,
        @NotBlank(message = "otp not blank") String otp,
        @NotNull(message = "type not null") OtpType type) {
}
