package com.dev.dungcony.modules.auth.dtos.req;

import com.dev.dungcony.modules.auth.enums.OtpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyOtpRegisterReq(
        @NotBlank(message = "email not blank") @Email(message = "incorrect format") String email,
        @NotBlank(message = "otp not blank") String otp) {
}
