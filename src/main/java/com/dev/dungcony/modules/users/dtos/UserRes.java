package com.dev.dungcony.modules.users.dtos;

import java.util.UUID;

public record UserRes(
                UUID id,
                String firstName,
                String lastName,
                String avatar) {
}
