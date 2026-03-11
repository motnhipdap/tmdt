package com.dev.dungcony.modules.users.dtos.req;

public record AddressUpdateReq(
                Integer id,
                String country,
                String province,
                String district,
                String street,
                String detail) {

}
