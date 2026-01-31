package com.dev.dungcony.modules.users.dtos;

import com.dev.dungcony.modules.users.entities.User;

import java.util.UUID;

public record UserDto(
        UUID id,
        String fistName,
        String lastName,
        String img
) {
    public UserDto(User u) {
        this(u.getId(), u.getFirstName(), u.getLastName(), u.getImg());
    }

    public User toEntity() {
        User user = new User();
        if (id != null)
            user.setId(id);
        user.setFirstName(fistName);
        user.setLastName(lastName);
        user.setImg(img);
        return user;
    }

}
