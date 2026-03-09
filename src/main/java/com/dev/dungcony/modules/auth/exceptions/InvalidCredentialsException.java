package com.dev.dungcony.modules.auth.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Auth_incorrect_credential", "username or password incorrect");
    }

    public InvalidCredentialsException(String message) {
        super(HttpStatus.CONFLICT, "conflig", message);
    }
}
