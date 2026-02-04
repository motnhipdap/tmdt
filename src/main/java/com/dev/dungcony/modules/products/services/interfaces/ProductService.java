package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductBuyReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;

public interface ProductService {


    void addNew(ProductAddReq req);

    void buyProduct(ProductBuyReq req);

    void addQuantity(int id, int quantity);

    void delete(int pId);

    ProductBasicDto update(ProductUpdateReq req);

}
