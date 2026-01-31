package com.dev.dungcony.modules.users.dtos.req;

import com.dev.dungcony.modules.users.dtos.AddressDto;

import java.util.UUID;

public record AddressAddReq(
        UUID uuid,
        AddressDto address
) {
}
