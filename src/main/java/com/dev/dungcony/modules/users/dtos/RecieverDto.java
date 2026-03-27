package com.dev.dungcony.modules.users.dtos;

public record RecieverDto(
        AddressDto addr,
        String name,
        String phone
) {
}
