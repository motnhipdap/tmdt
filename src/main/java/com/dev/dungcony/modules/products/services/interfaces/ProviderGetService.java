package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.ProviderRes;

import java.util.List;

public interface ProviderGetService {

    ProviderRes getByCode(String code);

    ProviderRes getById(int id);

    ProviderRes getByName(String name);

    List<ProviderRes> getAll();
}
