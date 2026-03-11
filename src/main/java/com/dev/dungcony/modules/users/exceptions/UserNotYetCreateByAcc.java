package com.dev.dungcony.modules.users.exceptions;

import com.dev.dungcony.commons.exceptions.NotFoundException;

public class UserNotYetCreateByAcc extends NotFoundException {

    public UserNotYetCreateByAcc() {
        super("User profile has not been created");
    }

}
