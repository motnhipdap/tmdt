package com.dev.dungcony.modules.authorization.exceptions;

import org.springframework.http.HttpStatus;

public class SendEmailException extends AuthException {

    public SendEmailException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Auth_sys_500", "có lỗi khi gửi email");
    }
}
