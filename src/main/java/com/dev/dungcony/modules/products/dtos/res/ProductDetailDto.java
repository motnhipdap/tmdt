package com.dev.dungcony.modules.products.dtos.res;

import java.util.List;

public record ProductDetailDto(
        Integer id,
        String name,
        String description,
        int price,
        List<ProductImgDto> images,
        int quantity,
        int quantity_sold
) {
//    public ProductDetailDto(Product p, List<ProductImgDto> images) {
//        this(p.getId(), p.getName(), p.getDescription(), images, p.getQuantity(), p.getQuantitySold());
//    }
//
}
