package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.req.CategoryAddReq;

public interface CategoryCommandService {
    CategorySummaryDto addNew(CategoryAddReq category);

    void remove(String code);

    void remove(Integer id);
}
