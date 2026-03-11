package com.dev.dungcony.modules.users.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.dev.dungcony.modules.users.dtos.AddressRes;

public interface AddressGetService {

    AddressRes getAddressById(int id);

    List<AddressRes> getAddressByUserId(UUID userId);

}
