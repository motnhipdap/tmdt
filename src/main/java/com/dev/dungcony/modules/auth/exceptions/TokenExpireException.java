package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.UnauthorException;

public class TokenExpireException extends UnauthorException {

    public TokenExpireException() {
        super("auth_token_expire", "time limit");
    }

}
