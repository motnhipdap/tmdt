package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // check category có category con hay không
    boolean existsByParent_Id(Integer parentId);

    /**
     * Đếm số category tồn tại trong danh sách IDs.
     * Dùng khi validate categoryIds trước khi tạo promotion.
     */
    long countByIdIn(List<Integer> ids);

    /**
     * Cascade HIDDEN status cho tất cả sub-categories dựa trên path prefix.
     * Ví dụ: path = "/1/2/" sẽ HIDDEN tất cả category có path bắt đầu bằng "/1/2/"
     * (trừ chính nó vì đã được set ở service layer).
     */
    @Modifying
    @Query("UPDATE Category c SET c.status = 'HIDDEN' WHERE c.path LIKE CONCAT(:pathPrefix, '%') AND c.status = 'ACTIVE'")
    void hideAllByPathPrefix(@Param("pathPrefix") String pathPrefix);
}
