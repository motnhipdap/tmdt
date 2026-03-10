package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.ConflictException;


public class OtpIsInvalidException extends ConflictException {
    public OtpIsInvalidException() {
        super("Otp is invalid");
    }
}
