package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.enums.ProductSize;

public interface SizeCacheService {
    int getIdBySize(ProductSize size);

    ProductSize getProductSizeById(Integer id);
}
