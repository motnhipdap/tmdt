package com.dev.dungcony.modules.authorization.dtos.requests;

import com.dev.dungcony.modules.authorization.dtos.OtpType;

public record SendOtpReq(String email, OtpType otpType) {
}
