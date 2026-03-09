package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.req.SendOtpChangeEmailReq;

public interface SendOtpService {
    void sendOtpForgotPassword(String email);

    void sendOtpRegister(String email);

    void sendOtpChangeEmail(int accid, SendOtpChangeEmailReq req);

}
