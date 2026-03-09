package com.dev.dungcony.modules.auth.dtos.req;

public record SendOtpChangeEmailReq(
        String email,
        String username
) {
}
