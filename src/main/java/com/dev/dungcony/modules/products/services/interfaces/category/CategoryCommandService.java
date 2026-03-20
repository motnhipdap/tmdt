package com.dev.dungcony.modules.products.services.interfaces.category;

import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.req.CategoryAddReq;

public interface CategoryCommandService {
    CategorySummaryDto addNew(CategoryAddReq category);

    void delete(String code);
}
