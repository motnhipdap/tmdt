package com.dev.dungcony.modules.users.services.impl;

import com.dev.dungcony.commons.exceptions.NotFoundException;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.users.dtos.UserDto;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.repositories.UserRepository;
import com.dev.dungcony.modules.users.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(int accId, UserDto req) {
        User user = req.toEntity();
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);

        Account accountRef = new Account();
        accountRef.setId(accId);
        user.setAccount(accountRef);

        userRepository.save(user);

        return new UserDto(user);
    }

    @Override
    public UserDto getUser(int accId) {
        User user = userRepository.findByAccount_Id(accId)
                .orElseThrow(() -> new NotFoundException("user profile has not been created"));

        return new UserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(int accId, UserDto dto) {

        UUID uuid = dto.id();
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("not found user with id " + uuid));

        if (user.getAccount() == null || user.getAccount().getId() != accId)
            user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setImg(dto.img());

        userRepository.save(user);

        return new UserDto(user);
    }

}
