package com.dev.dungcony.commons.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "404", message);
    }

    public NotFoundException(String code, String message) {
        super(HttpStatus.NOT_FOUND, code, message);
    }
}
