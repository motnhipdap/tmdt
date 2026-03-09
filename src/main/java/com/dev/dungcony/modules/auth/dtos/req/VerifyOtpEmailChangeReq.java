package com.dev.dungcony.modules.auth.dtos.req;

public record VerifyOtpEmailChangeReq(
        String newEmail,
        String username,
        String otp
) {
}
