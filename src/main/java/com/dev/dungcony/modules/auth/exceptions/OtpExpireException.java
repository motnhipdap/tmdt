package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import com.dev.dungcony.commons.exceptions.UnauthorException;
import org.springframework.http.HttpStatus;

public class OtpExpireException extends UnauthorException {
    public OtpExpireException() {
        super("otp-exp", "Otp đã hết hạn");
    }
}
