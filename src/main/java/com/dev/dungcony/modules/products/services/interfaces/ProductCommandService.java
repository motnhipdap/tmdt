package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;

public interface ProductCommandService {


    ProductDetailRes addNew(ProductAddReq req);

    void addQuantity(int id, int quantity);

    void delete(int pId);

    ProductDetailRes update(ProductUpdateReq req);

}
