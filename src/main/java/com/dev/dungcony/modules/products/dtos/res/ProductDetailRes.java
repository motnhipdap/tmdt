package com.dev.dungcony.modules.products.dtos.res;

import java.math.BigDecimal;
import java.time.Instant;

import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.ProviderSummaryDto;
import com.dev.dungcony.modules.products.enums.ProductStatus;

public record ProductDetailRes(
                Integer id,

                String name,
                String productCode,
                String description,

                BigDecimal price,
                Integer quantity,
                Integer sold,

                Float rating,

                String imgUrl,
                ProductStatus status,

                Instant createdAt,
                Instant updatedAt,
                CategorySummaryDto category,
                ProviderSummaryDto provider) {
}
