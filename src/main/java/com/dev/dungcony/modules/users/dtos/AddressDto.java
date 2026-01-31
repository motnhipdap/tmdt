package com.dev.dungcony.modules.users.dtos;

import com.dev.dungcony.modules.users.entities.Address;

public record AddressDto(
        Integer id,
        String country,
        String province,
        String district,
        String street,
        String detail
) {

    public Address toEntity() {
        Address address = new Address();
        if (id != null)
            address.setId(id);

        address.setCountry(country);
        address.setProvince(province);
        address.setDistrict(district);
        address.setStreet(street);
        address.setDetail(detail);
        return address;
    }

    public AddressDto(Address address) {
        this(address.getId(), address.getCountry(), address.getProvince(), address.getDistrict(), address.getStreet(), address.getDetail());
    }
}
