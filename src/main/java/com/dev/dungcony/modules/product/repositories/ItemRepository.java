package com.dev.dungcony.modules.product.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.dungcony.modules.product.entities.Item;
import com.dev.dungcony.modules.product.entities.ItemId;

public interface ItemRepository extends JpaRepository<Item, ItemId> {
    List<Item> findByIdProductId(Integer productId);

    List<Item> findByIdSizeId(Integer sizeId);

    @Query("""
            select i
            from Item i
            join fetch i.product
            join fetch i.size
            where i.id = :id
            """)
    Optional<Item> findDetailByIdWithProductAndSize(ItemId id);

    @Query("""
            select i.id.sizeId
            from Item i
            where i.product.code = :productCode
            """)
    List<Integer> findSizesByProductCode(String productCode);
}
