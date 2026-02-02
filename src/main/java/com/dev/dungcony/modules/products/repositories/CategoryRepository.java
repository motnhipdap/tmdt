package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // check category có category con hay không
    boolean existsByParent_Id(Integer parentId);
}
