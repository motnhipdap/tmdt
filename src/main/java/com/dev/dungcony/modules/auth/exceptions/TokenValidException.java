package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class TokenValidException extends AppException {
    public TokenValidException() {
        super(HttpStatus.UNAUTHORIZED, "Auth_Token_is_invalid", "not permitted");
    }
}
