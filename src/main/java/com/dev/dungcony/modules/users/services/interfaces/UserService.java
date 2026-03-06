package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.UserDto;

public interface UserService {
    UserDto createUser(int accId, UserDto req);

    UserDto getUser(int accId);

    UserDto updateUser(int accId, UserDto dto);
}
