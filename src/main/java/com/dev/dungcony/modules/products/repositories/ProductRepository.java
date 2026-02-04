package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.dtos.ProductBasicInterface;
import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

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

    @Query(value = """
            WITH RECURSIVE cate_tree AS (
                SELECT id
                FROM tbl_categories
                WHERE id = :categoryId
                UNION ALL
                SELECT c.id
                FROM tbl_categories c
                JOIN cate_tree ct ON c.parent_id = ct.id
            )
            SELECT
                p.id AS id,
                p.name AS name,
                p.price AS price,
                p.rated AS rated,
                img.image_url AS image
            FROM tbl_products p
            LEFT JOIN tbl_product_img img
                ON img.product_id = p.id AND img.is_main = true
            WHERE p.category_id IN (
                SELECT id FROM cate_tree
            )
            """,
            nativeQuery = true)
    Page<ProductBasicInterface> findAllByCategoryTree(
            @Param("categoryId") Integer categoryId,
            Pageable pageable
    );

    @Query("""
                select img.imageUrl
                from ProductImg img
                where img.product.id = :productId
                  and img.isMain = true
            """)
    Optional<String> findMainImage(@Param("productId") Integer productId);

}
