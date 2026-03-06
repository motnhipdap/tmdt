package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.req.ProviderAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProviderUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProviderRes;

public interface ProviderCommandService {
    ProviderRes addNew(ProviderAddReq dto);

    void delete(String code);

    ProviderRes update(String code, ProviderUpdateReq dto);
}
