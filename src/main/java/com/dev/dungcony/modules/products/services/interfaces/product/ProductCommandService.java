package com.dev.dungcony.modules.products.services.interfaces.product;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;

public interface ProductCommandService {

    ProductDetailRes addNew(ProductAddReq req);

    void addQuantity(String code, int quantity);

    void delete(String code);

    ProductDetailRes update(ProductUpdateReq req);
}
