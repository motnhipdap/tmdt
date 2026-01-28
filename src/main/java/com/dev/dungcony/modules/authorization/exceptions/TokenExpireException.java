package com.dev.dungcony.modules.authorization.exceptions;

import org.springframework.http.HttpStatus;

import com.dev.dungcony.commons.exceptions.AppException;

public class TokenExpireException extends AppException {

    public TokenExpireException() {
        super(HttpStatus.UNAUTHORIZED, "auth_token_expire", "time limit");
    }

}
