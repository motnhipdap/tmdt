package com.dev.dungcony.modules.users.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.dev.dungcony.modules.users.dtos.AddressDto;

public interface AddressGetService {

    AddressDto getAddressById(int id);

    List<AddressDto> getAddressByUserId(UUID userId);

}
