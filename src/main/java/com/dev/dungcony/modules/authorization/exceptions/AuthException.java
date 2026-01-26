package com.dev.dungcony.modules.authorization.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public abstract class AuthException extends AppException {

    protected AuthException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }
}
