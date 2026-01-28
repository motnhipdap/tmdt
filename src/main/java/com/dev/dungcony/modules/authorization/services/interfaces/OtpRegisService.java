package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.requests.VerifyOtpReq;

public interface OtpRegisService {
    void sendOtp(String email);

    boolean verifyOTP(VerifyOtpReq req);

}
