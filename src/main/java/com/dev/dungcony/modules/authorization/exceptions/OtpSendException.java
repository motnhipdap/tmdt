package com.dev.dungcony.modules.authorization.exceptions;

import org.springframework.http.HttpStatus;

public class OtpSendException extends AuthException {

    public OtpSendException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Auth_sys_500", "có lỗi khi gửi OTP");
    }
}
