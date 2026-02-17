package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.ProviderAddRes;

public interface ProviderService {
    ProviderAddRes addNew(ProviderAddRes dto);

    void delete(int id);

    void update(ProviderAddRes dto);
}
