package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepsitory extends JpaRepository<Product, Integer> {

    @Query("""
                SELECT new com.dev.dungcony.modules.products.dtos.res.ProductBasicDto(
                    p.id,
                    p.name,
                    p.price,
                    p.rated,
                    img.imageUrl
                )
                FROM Product p
                LEFT JOIN ProductImg img
                    ON img.product = p AND img.isMain = true
                WHERE p.status = :status
            """)
    Page<ProductBasicDto> findProductList(
            @Param("status") ProductStatus status,
            Pageable pageable
    );

    @Query("""
                SELECT new com.dev.dungcony.modules.products.dtos.res.ProductBasicDto(
                    p.id,
                    p.name,
                    p.price,
                    p.rated,
                    img.imageUrl
                )
                FROM Product p
                LEFT JOIN ProductImg img
                    ON img.product = p AND img.isMain = true
                WHERE p.status = :status
            """)
    Page<ProductBasicDto> getAllByKeyword(
            @Param("status") ProductStatus status,
            @Param("key") String key,
            Pageable pageable
    );

    @Transactional
    @Modifying
    @Query("""
                update Product p
                set p.quantity = :quantity
                where p.id = :id
            """)
    int updateQuantityProduct(
            @Param("id") int id,
            @Param("quantity") int quantity
    );
}
