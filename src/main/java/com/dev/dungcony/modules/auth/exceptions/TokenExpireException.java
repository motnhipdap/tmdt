package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import com.dev.dungcony.commons.exceptions.UnauthorException;
import org.springframework.http.HttpStatus;

public class TokenExpireException extends UnauthorException {

    public TokenExpireException() {
        super("auth_token_expire", "time limit");
    }

}
