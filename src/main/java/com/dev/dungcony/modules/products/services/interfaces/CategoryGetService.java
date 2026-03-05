package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.CategoryRes;

import java.util.List;

public interface CategoryGetService {
    List<CategoryRes> getAllChildren(String path);

    List<CategoryRes> getAll();

    CategoryRes getByCode(String code);

    CategoryRes getByName(String name);
}
