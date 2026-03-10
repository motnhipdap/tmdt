package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class SendEmailException extends AppException {

    public SendEmailException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Auth_sys_500", "có lỗi khi gửi email");
    }
}
