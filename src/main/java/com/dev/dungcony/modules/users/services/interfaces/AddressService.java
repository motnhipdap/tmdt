package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.AddressDto;
import com.dev.dungcony.modules.users.dtos.req.AddressUpdateReq;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;

public interface AddressService {

    AddressDto addNew(AddressAddReq req);

    AddressDto update(AddressUpdateReq dto);

    void delete(int id);
}
