package com.dev.dungcony.modules.authorization.dtos.requests;

import com.dev.dungcony.modules.authorization.dtos.OtpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpReq(
        @NotBlank(message = "email not blank")
        @Email(message = "incorrect format")
        String email,
        String otp,
        @NotBlank(message = "type not blank")
) {
}
