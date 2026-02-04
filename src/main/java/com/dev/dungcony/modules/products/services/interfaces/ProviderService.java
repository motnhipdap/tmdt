package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.ProviderDto;

public interface ProviderService {
    void addNew(ProviderDto dto);

    void delete(int id);

    void update(ProviderDto dto);
}
