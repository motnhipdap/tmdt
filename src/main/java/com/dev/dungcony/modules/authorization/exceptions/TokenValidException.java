package com.dev.dungcony.modules.authorization.exceptions;

import org.springframework.http.HttpStatus;

public class TokenValidException extends AuthException {
    public TokenValidException() {
        super(HttpStatus.UNAUTHORIZED, "Auth_Token_is_invalid", "not permitted");
    }
}
