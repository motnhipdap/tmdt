package com.dev.dungcony.modules.auth.exceptions;

import org.springframework.http.HttpStatus;

public class EmailExistException extends AuthException {

    public EmailExistException() {
        super(HttpStatus.CONFLICT, "Auth_conflict_email", "Email already exists");
    }
}
