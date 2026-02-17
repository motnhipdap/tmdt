package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.res.ProviderAddRes;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.enums.ProviderStatus;
import com.dev.dungcony.modules.products.exceptions.ProductConfligException;
import com.dev.dungcony.modules.products.exceptions.ProviderNotFoundException;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProviderCommandServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;

    @Override
    public void addNew(ProviderAddRes dto) {
        if (providerRepository.existsById(dto.id()))
            throw new ProductConfligException();

        providerRepository.save(dto.getEntity(dto));
    }

    @Override
    public void delete(int id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(ProviderNotFoundException::new);

        provider.setStatus(ProviderStatus.INACTIVE);
        providerRepository.deleteById(id);
    }

    @Override
    public void update(ProviderAddRes dto) {
        if (!providerRepository.existsById(dto.id()))
            throw new ProviderNotFoundException();

        providerRepository.save(dto.getEntity(dto));
    }
}
