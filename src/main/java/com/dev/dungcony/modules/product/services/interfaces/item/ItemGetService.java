package com.dev.dungcony.modules.product.services.interfaces.item;

import java.util.List;

import com.dev.dungcony.modules.product.dtos.ItemDto;
import com.dev.dungcony.modules.product.dtos.res.ItemRes;
import com.dev.dungcony.modules.product.enums.ProductSize;
import com.dev.dungcony.modules.promotion.dtos.IteamCartDto;

public interface ItemGetService {
    List<ItemDto> getByProductId(Integer productId);

    List<ItemDto> getByProductCode(String productCode);

    List<ItemDto> getBySizeId(Integer sizeId);

    ItemRes getByProductCodeAndSize(String productCode, ProductSize productSize);

    List<ProductSize> getSizesByProductCode(String productCode);
}
