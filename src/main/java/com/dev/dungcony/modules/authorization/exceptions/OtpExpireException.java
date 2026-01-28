package com.dev.dungcony.modules.authorization.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class OtpExpireException extends AppException {

    public OtpExpireException() {
        super(HttpStatus.BAD_GATEWAY, "422", "Otp đã hết hạn");
    }
}
