package com.dev.dungcony.modules.products.exceptions;

import com.dev.dungcony.commons.exceptions.ConflictException;

public class ProviderConfilctException extends ConflictException {
    public ProviderConfilctException(String mes) {
        super("CONFLICT", mes);
    }

    public ProviderConfilctException(String code, String mes) {
        super(code, mes);
    }

    public ProviderConfilctException() {
        super("Provider is already exist");
    }
}
