package com.dev.dungcony.modules.users.mappers;

import com.dev.dungcony.modules.users.dtos.AddressRes;
import com.dev.dungcony.modules.users.dtos.req.AddressUpdateReq;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;
import com.dev.dungcony.modules.users.entities.Address;

public class AddressMapper {

    public static AddressRes toDto(Address address) {
        return new AddressRes(
                address.getId(),
                address.getCountry(),
                address.getProvince(),
                address.getDistrict(),
                address.getStreet(),
                address.getDetail());
    }

    public static Address toEntity(AddressRes dto) {
        Address address = new Address();
        if (dto.id() != null)
            address.setId(dto.id());
        address.setCountry(dto.country());
        address.setProvince(dto.province());
        address.setDistrict(dto.district());
        address.setStreet(dto.street());
        address.setDetail(dto.detail());
        return address;
    }

    public static Address toEntity(AddressAddReq dto) {
        Address address = new Address();
        address.setCountry(dto.country());
        address.setProvince(dto.province());
        address.setDistrict(dto.district());
        address.setStreet(dto.street());
        address.setDetail(dto.detail());
        return address;
    }

    public static Address toEntity(AddressUpdateReq dto) {
        Address address = new Address();
        address.setId(dto.id());
        address.setCountry(dto.country());
        address.setProvince(dto.province());
        address.setDistrict(dto.district());
        address.setStreet(dto.street());
        address.setDetail(dto.detail());
        return address;
    }
}