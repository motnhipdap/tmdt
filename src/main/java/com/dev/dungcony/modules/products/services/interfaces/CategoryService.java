package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.req.CategoryAddReq;

public interface CategoryService {
    void addCategory(CategoryAddReq category);

    void removeCategory(Integer id);
}
