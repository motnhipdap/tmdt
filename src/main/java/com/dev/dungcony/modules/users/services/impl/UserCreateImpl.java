package com.dev.dungcony.modules.users.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dev.dungcony.modules.users.dtos.UserRes;
import com.dev.dungcony.modules.users.dtos.req.UserCreateReq;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.mappers.UserMapper;
import com.dev.dungcony.modules.users.repositories.UserRepository;
import com.dev.dungcony.modules.users.services.interfaces.UserCreateService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCreateImpl implements UserCreateService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserRes createUser(int accId, UserCreateReq req) {
        User user = new User();
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setAvatar(req.avatar());

        UUID uuid = UUID.randomUUID();
        user.setId(uuid);

        user.setAccountId(accId);

        userRepository.save(user);

        return UserMapper.toUserDto(user);
    }

}
