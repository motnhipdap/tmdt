package com.dev.dungcony.modules.authorization.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsEx extends AuthException {
    protected InvalidCredentialsEx() {
        super(HttpStatus.UNAUTHORIZED, "Auth_incorrect_credential", "username or password incorrect");
    }
}
