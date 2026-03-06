package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.res.ProviderRes;
import com.dev.dungcony.modules.products.exceptions.ProviderNotFoundException;
import com.dev.dungcony.modules.products.mappers.ProviderMapper;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProviderGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProviderGetServiceImpl implements ProviderGetService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    @Override
    public ProviderRes getByCode(String code) {
        return providerMapper.toRes(
                providerRepository.findByCode(code).orElseThrow(ProviderNotFoundException::new));
    }

    @Override
    public ProviderRes getByName(String name) {
        return providerMapper.toRes(
                providerRepository.findByName(name).orElseThrow(ProviderNotFoundException::new));
    }

    @Override
    public List<ProviderRes> getAll() {
        return providerRepository.findAll().stream().map(providerMapper::toRes).toList();
    }
}
