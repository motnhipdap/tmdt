package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.ConflictException;

public class OtpInvalidException extends ConflictException {
    public OtpInvalidException() {
        super("Otp is invalid");
    }
}
