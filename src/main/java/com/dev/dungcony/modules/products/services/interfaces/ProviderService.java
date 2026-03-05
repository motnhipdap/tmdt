package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.req.ProviderAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProviderUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProviderRes;

public interface ProviderService {
    ProviderRes addNew(ProviderAddReq dto);

    void delete(int id);

    ProviderRes update(ProviderUpdateReq dto);
}
