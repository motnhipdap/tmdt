package com.dev.dungcony.modules.authorization.dtos;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OtpType {
    // OTP 6 số
    REGISTER("otp:register"),
    FORGOT_PASSWORD("otp:forgot_password"),

    // Email change tokens (JWT)
    EMAIL_CHANGE_OLD("email_change:old"),
    EMAIL_CHANGE_NEW("email_change:new");

    @JsonValue
    private final String value;
}