package com.dev.dungcony.modules.products.mappers;

import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.res.CategoryRes;
import com.dev.dungcony.modules.products.entities.Category;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi giữa Category entity và các DTO response.
 * Không bao gồm internal id — client dùng code làm định danh.
 */
@Component
public class CategoryMapper {

    public CategoryRes toRes(Category c) {
        return new CategoryRes(
                c.getName(),
                c.getCode(),
                c.getStatus(),
                c.getDescription(),
                c.getImgUrl());
    }

    public CategorySummaryDto toSummaryDto(Category c) {
        return new CategorySummaryDto(c.getName(), c.getCode());
    }
}
