package com.dev.dungcony.modules.products.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.dungcony.modules.products.entities.Item;
import com.dev.dungcony.modules.products.entities.ItemId;

public interface ItemRepository extends JpaRepository<Item, ItemId> {
    List<Item> findByIdProductId(Integer productId);

    List<Item> findByIdSizeId(Integer sizeId);

    @Query("""
            select i.id.sizeId
            from Item i
            where i.product.code = :productCode
            """)
    List<Integer> findSizesByProductCode(String productCode);
}
