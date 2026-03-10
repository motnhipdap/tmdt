package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.ConflictException;

public class EmailExistException extends ConflictException {

    public EmailExistException() {
        super("Conflict_email", "Email already exists");
    }
}
