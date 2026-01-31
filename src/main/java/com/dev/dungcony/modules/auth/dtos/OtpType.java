package com.dev.dungcony.modules.auth.dtos;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OtpType {
    // OTP 6 số
    REGISTER("otp:register"),
    FORGOT_PASSWORD("otp:forgot_password");
    @JsonValue
    private final String value;
}