package com.dev.dungcony.modules.users.services.impl;

import com.dev.dungcony.modules.users.dtos.AddressRes;
import com.dev.dungcony.modules.users.dtos.req.AddressUpdateReq;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;
import com.dev.dungcony.modules.users.entities.Address;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.exceptions.AddressIdConflict;
import com.dev.dungcony.modules.users.exceptions.AddressNotFound;
import com.dev.dungcony.modules.users.mappers.AddressMapper;
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
    public AddressRes addNew(AddressAddReq req) {
        Address address = new Address();
        address.setCountry(req.country());
        address.setProvince(req.province());
        address.setDistrict(req.district());
        address.setStreet(req.street());
        address.setDetail(req.detail());

        User user = new User();
        user.setId(req.uuid());

        address.setUser(user);

        addressRepository.save(address);

        return AddressMapper.toDto(address);
    }

    @Override
    public AddressRes update(AddressUpdateReq req) {

        Address address = addressRepository.findById(req.id())
                .orElseThrow(AddressNotFound::new);
        if (req.country() != null)
            address.setCountry(req.country());

        if (req.province() != null)
            address.setProvince(req.province());

        if (req.district() != null)
            address.setDistrict(req.district());

        if (req.street() != null)
            address.setStreet(req.street());

        if (req.detail() != null)
            address.setDetail(req.detail());

        addressRepository.save(address);

        return AddressMapper.toDto(address);
    }

    @Override
    public void delete(int id) {
        if (addressRepository.deleteByIdReturnCount(id) == 0) {
            throw new AddressIdConflict();
        }

        addressRepository.deleteById(id);
    }

}
