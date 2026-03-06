package com.dev.dungcony.modules.users.services.impl;

import com.dev.dungcony.commons.exceptions.NotFoundException;
import com.dev.dungcony.modules.users.dtos.AddressDto;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;
import com.dev.dungcony.modules.users.entities.Address;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.repositories.AddressRepository;
import com.dev.dungcony.modules.users.services.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    @Override
    public AddressDto addAddress(AddressAddReq req) {
        Address address = req.address().toEntity();
        User user = new User();
        user.setId(req.uuid());

        address.setUser(user);

        addressRepository.save(address);

        return new AddressDto(address);
    }

    @Override
    public AddressDto updateAddress(AddressDto req) {
        Address address = addressRepository.findById(req.id())
                .orElseThrow(() -> new NotFoundException("id address khong hop le"));

        Address addressUpdate = req.toEntity();
        addressUpdate.setUser(address.getUser());

        addressRepository.save(addressUpdate);

        return new AddressDto(addressUpdate);
    }

    @Override
    public void deleteAddress(int id) {
        if (addressRepository.deleteByIdReturnCount(id) == 0) {
            throw new NotFoundException("id address khong hop le");
        }
    }
}
