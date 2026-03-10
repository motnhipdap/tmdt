package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class UsernameExistsException extends AppException {
    public UsernameExistsException() {
        super(HttpStatus.CONFLICT, "Auth_conflict_username", "username already exists");
    }
}
