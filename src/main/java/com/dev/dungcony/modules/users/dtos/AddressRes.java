package com.dev.dungcony.modules.users.dtos;

public record AddressRes(
        Integer id,
        String country,
        String province,
        String district,
        String street,
        String detail) {

}
