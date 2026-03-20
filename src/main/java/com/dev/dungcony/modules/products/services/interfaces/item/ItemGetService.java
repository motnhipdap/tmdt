package com.dev.dungcony.modules.products.services.interfaces.item;

import java.util.List;

import com.dev.dungcony.modules.products.dtos.ItemDto;
import com.dev.dungcony.modules.products.enums.ProductSize;

public interface ItemGetService {
    List<ItemDto> getByProductId(Integer productId);

    List<ItemDto> getByProductCode(String productCode);

    List<ItemDto> getBySizeId(Integer sizeId);

    ItemDto getByProductCodeAndSizeId(String productCode, Integer sizeId);

    List<ProductSize> getSizesByProductCode(String productCode);
}
