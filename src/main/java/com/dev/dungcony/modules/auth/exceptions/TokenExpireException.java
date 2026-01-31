package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class TokenExpireException extends AppException {

    public TokenExpireException() {
        super(HttpStatus.UNAUTHORIZED, "auth_token_expire", "time limit");
    }

}
