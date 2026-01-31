package com.dev.dungcony.modules.auth.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameExistsException extends AuthException {
    public UsernameExistsException() {
        super(HttpStatus.CONFLICT, "Auth_conflict_username", "username already exists");
    }
}
