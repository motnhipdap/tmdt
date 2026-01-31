package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.AddressDto;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;

public interface AddressService {
    AddressDto addAddress(AddressAddReq req);

    AddressDto updateAddress(AddressDto dto);

    void deleteAddress(int id);
}
