package com.dev.dungcony.modules.products.dtos;

import com.dev.dungcony.modules.products.dtos.res.ProductImgDto;
import com.dev.dungcony.modules.products.entities.Product;

import java.util.List;

public record ProductDetailDto(
        Integer id,
        String name,
        String description,
        List<ProductImgDto> images,
        int quantity,
        int quantity_sold
) {
    public ProductDetailDto(Product p, List<ProductImgDto> images) {
        this(p.getId(), p.getName(), p.getDescription(), images, p.getQuantity(), p.getQuantitySold());
    }

    public Product getEntities(ProductDetailDto dto) {
        Product product = new Product();
        product.setId(dto.id());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setQuantity(dto.quantity());
        product.setQuantitySold(dto.quantity_sold());

        return product;
    }
}
