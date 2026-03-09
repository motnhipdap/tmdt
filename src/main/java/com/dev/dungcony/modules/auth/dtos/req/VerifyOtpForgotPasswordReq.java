package com.dev.dungcony.modules.auth.dtos.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpForgotPasswordReq(
        @NotBlank(message = "email not blank") @Email(message = "incorrect format") String email,
        @NotBlank(message = "otp not blank") String otp
) {
}
