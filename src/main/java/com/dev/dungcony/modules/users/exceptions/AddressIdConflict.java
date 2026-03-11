package com.dev.dungcony.modules.users.exceptions;

import com.dev.dungcony.commons.exceptions.ConflictException;

public class AddressIdConflict extends ConflictException {
    public AddressIdConflict() {
        super("Address id conflict");
    }

}
