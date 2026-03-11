package com.dev.dungcony.modules.users.services.interfaces;

import java.util.UUID;

import com.dev.dungcony.modules.users.dtos.UserRes;

public interface UserGetService {
    UserRes getUserByAccId(int accId);

    UserRes getUserById(UUID id);

    UserRes getByFirstName(String firstName);

    UserRes getByLastName(String lastName);
}
