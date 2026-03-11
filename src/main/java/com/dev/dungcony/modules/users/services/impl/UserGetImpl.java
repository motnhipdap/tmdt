package com.dev.dungcony.modules.users.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dev.dungcony.modules.users.dtos.UserRes;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.exceptions.UserNotYetCreateByAcc;
import com.dev.dungcony.modules.users.mappers.UserMapper;
import com.dev.dungcony.modules.users.repositories.UserRepository;
import com.dev.dungcony.modules.users.services.interfaces.UserGetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserGetImpl implements UserGetService {

    private final UserRepository userRepository;

    @Override
    public UserRes getUserByAccId(int accId) {
        User user = userRepository.findByAccountId(accId)
                .orElseThrow(UserNotYetCreateByAcc::new);

        return UserMapper.toUserDto(user);
    }

    @Override
    public UserRes getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotYetCreateByAcc::new);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserRes getByFirstName(String firstName) {
        User user = userRepository.findByFirstName(firstName)
                .orElseThrow(UserNotYetCreateByAcc::new);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserRes getByLastName(String lastName) {
        User user = userRepository.findByLastName(lastName)
                .orElseThrow(UserNotYetCreateByAcc::new);
        return UserMapper.toUserDto(user);
    }

}
