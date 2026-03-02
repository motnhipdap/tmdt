package com.dev.dungcony.modules.products.dtos.res;

import java.math.BigDecimal;

import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.ProviderSummaryDto;

public record ProductSumaryRes(
        Integer id,
        String name,
        String productCode,
        BigDecimal price,
        Float rated,

        String imgUrl,

        CategorySummaryDto category,
        ProviderSummaryDto provider) {

}
