package com.dev.dungcony.modules.authorization.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class OtpExpireException extends AppException {

    public OtpExpireException() {
        super(HttpStatus.GONE, "410", "Otp đã hết hạn");
    }
}
