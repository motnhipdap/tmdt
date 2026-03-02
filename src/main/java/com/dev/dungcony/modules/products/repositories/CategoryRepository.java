package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // check category có category con hay không
    boolean existsByParent_Id(Integer parentId);

    /**
     * Đếm số category tồn tại trong danh sách IDs.
     * Dùng khi validate categoryIds trước khi tạo promotion.
     */
    long countByIdIn(List<Integer> ids);
}
