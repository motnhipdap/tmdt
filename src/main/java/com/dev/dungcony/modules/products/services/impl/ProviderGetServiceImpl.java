package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.res.ProviderRes;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.exceptions.ProviderNotFoundException;
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

    @Override
    public ProviderRes getByCode(String code) {
        Provider provider = providerRepository.findByCode(code)
                .orElseThrow(ProviderNotFoundException::new);

        return toRes(provider);
    }

    @Override
    public ProviderRes getById(int id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(ProviderNotFoundException::new);

        return toRes(provider);
    }

    @Override
    public ProviderRes getByName(String name) {
        Provider prov = providerRepository.findByName(name)
                .orElseThrow(ProviderNotFoundException::new);

        return toRes(prov);
    }

    @Override
    public List<ProviderRes> getAll() {
        List<Provider> providers = providerRepository.findAll();

        return providers.stream().map(this::toRes).toList();
    }

    private ProviderRes toRes(Provider p) {
        return new ProviderRes(
                p.getName(),
                p.getCode(),
                p.getEmail(),
                p.getPhone(),
                p.getDescription(),
                p.getLogo());
    }
}
