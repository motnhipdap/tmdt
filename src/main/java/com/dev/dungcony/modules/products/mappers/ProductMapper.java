package com.dev.dungcony.modules.products.mappers;

import com.dev.dungcony.commons.dtos.DiscountInfoDto;
import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.ProviderSummaryDto;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.Provider;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi giữa Product entity và các DTO response.
 * Không bao gồm internal id — client dùng code làm định danh.
 */
@Component
public class ProductMapper {

    /**
     * Entity → ProductDetailRes không có discount (cho create/update response).
     */
    public ProductDetailRes toDetailRes(Product p) {
        return toDetailRes(p, null);
    }

    /**
     * Entity → ProductDetailRes với discount info (cho get detail response).
     */
    public ProductDetailRes toDetailRes(Product p, DiscountInfoDto discount) {
        CategorySummaryDto catDto = null;
        Category c = p.getCategory();
        if (c != null) {
            catDto = new CategorySummaryDto(c.getName(), c.getCode());
        }

        ProviderSummaryDto provDto = null;
        Provider pv = p.getProvider();
        if (pv != null) {
            provDto = new ProviderSummaryDto(pv.getName(), pv.getCode());
        }

        return new ProductDetailRes(
                p.getName(),
                p.getCode(),
                p.getDescription(),
                p.getPrice(),
                discount != null ? discount.finalPrice() : p.getPrice(),
                discount != null ? discount.discountType() : "NONE",
                discount != null ? discount.discountValue() : 0,
                p.getQuantity(),
                p.getQuantitySold(),
                p.getRated(),
                p.getImg(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdateAt(),
                catDto,
                provDto);
    }
}
