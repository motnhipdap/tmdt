package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.req.ProviderAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProviderUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProviderRes;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.enums.ProviderStatus;
import com.dev.dungcony.modules.products.exceptions.ProviderConfilctException;
import com.dev.dungcony.modules.products.exceptions.ProviderNotFoundException;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProviderCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProviderCommandServiceImpl implements ProviderCommandService {

    private final ProviderRepository providerRepository;

    @Transactional
    @Override
    public ProviderRes addNew(ProviderAddReq dto) {
        Provider provider = new Provider();
        provider.setName(dto.name());
        provider.setCode(dto.providerCode());
        provider.setEmail(dto.email());
        provider.setPhone(dto.phone());
        provider.setDescription(dto.description());
        provider.setLogo(dto.img());

        providerRepository.save(provider);

        return toRes(provider);
    }

    @Transactional
    @Override
    public void delete(String code) {
        Provider provider = providerRepository.findByCode(code)
                .orElseThrow(ProviderNotFoundException::new);

        if (provider.getStatus() == ProviderStatus.INACTIVE) {
            throw new ProviderConfilctException("provider is already inactive");
        }

        provider.setStatus(ProviderStatus.INACTIVE);
    }

    @Transactional
    @Override
    public ProviderRes update(String code, ProviderUpdateReq dto) {
        Provider provider = providerRepository.findByCode(code)
                .orElseThrow(ProviderNotFoundException::new);

        if (provider.getStatus() == ProviderStatus.INACTIVE) {
            throw new ProviderConfilctException("cannot update inactive provider");
        }

        if (dto.name() != null)
            provider.setName(dto.name());
        if (dto.email() != null)
            provider.setEmail(dto.email());
        if (dto.phone() != null)
            provider.setPhone(dto.phone());
        if (dto.description() != null)
            provider.setDescription(dto.description());
        if (dto.img() != null)
            provider.setLogo(dto.img());

        return toRes(provider);
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
