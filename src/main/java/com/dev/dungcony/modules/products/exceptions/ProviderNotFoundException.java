package com.dev.dungcony.modules.products.exceptions;

import com.dev.dungcony.commons.exceptions.NotFoundException;

public class ProviderNotFoundException extends NotFoundException {
    public ProviderNotFoundException() {
        super("PROVIDER_NOT_FOUND", "Provider not found");
    }
}
