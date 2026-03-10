package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.InvalidException;

public class InvalidUsernameOrPassword extends InvalidException {
    public InvalidUsernameOrPassword() {
        super("Auth_incorrect_credential", "username or password incorrect");
    }
}
