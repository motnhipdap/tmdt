package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.AddressRes;
import com.dev.dungcony.modules.users.dtos.req.AddressUpdateReq;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;

public interface AddressService {

    AddressRes addNew(AddressAddReq req);

    AddressRes update(AddressUpdateReq dto);

    void delete(int id);
}
