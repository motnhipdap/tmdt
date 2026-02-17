package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductAddRes;
import com.dev.dungcony.modules.products.dtos.res.ProductUpdateRes;

public interface ProductCommandService {


    ProductAddRes addNew(ProductAddReq req);

    void addQuantity(int id, int quantity);

    void delete(int pId);

    ProductUpdateRes update(ProductUpdateReq req);

}
