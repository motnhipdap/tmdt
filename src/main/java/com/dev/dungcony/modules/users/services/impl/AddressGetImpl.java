package com.dev.dungcony.modules.users.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dev.dungcony.modules.users.dtos.AddressRes;
import com.dev.dungcony.modules.users.entities.Address;
import com.dev.dungcony.modules.users.exceptions.AddressIdConflict;
import com.dev.dungcony.modules.users.mappers.AddressMapper;
import com.dev.dungcony.modules.users.repositories.AddressRepository;
import com.dev.dungcony.modules.users.services.interfaces.AddressGetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressGetImpl implements AddressGetService {

    private final AddressRepository addressRepository;

    @Override
    public List<AddressRes> getAddressByUserId(UUID userId) {
        List<Address> addrs = addressRepository.findByUserId(userId);
        return addrs.stream()
                .map(AddressMapper::toDto)
                .toList();
    }

    @Override
    public AddressRes getAddressById(int id) {
        Address addr = addressRepository.findById(id)
                .orElseThrow(AddressIdConflict::new);
        return AddressMapper.toDto(addr);
    }

}
