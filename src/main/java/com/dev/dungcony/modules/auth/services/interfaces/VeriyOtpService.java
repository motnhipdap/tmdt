package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpEmailChangeReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;

public interface VeriyOtpService {
    boolean verifyOtpRegister(VerifyOtpReq req);

    boolean verifyOtpForgotPassword(VerifyOtpReq req);

    boolean verifyOtpEmailChange(int accid, VerifyOtpEmailChangeReq req);
}
