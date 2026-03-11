package com.dev.dungcony.modules.auth.exceptions;

import com.dev.dungcony.commons.exceptions.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException() {
        super("Account not found");
    }

}
