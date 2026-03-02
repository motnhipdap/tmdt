package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.dtos.ProductBasicInterface;
import com.dev.dungcony.modules.products.dtos.res.ProductAddRes;
import com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("""
                SELECT new com.dev.dungcony.modules.products.dtos.res.ProductAddRes(
                    p.id,
                    p.name,
                    p.price,
                    p.rated,
                    p.img,
                    p.category.id
                )
                FROM Product p
                WHERE p.status = :status
            """)
    Page<ProductSumaryRes> findProductList(
            @Param("status") ProductStatus status,
            Pageable pageable
    );

    /**
     * Tìm sản phẩm theo keyword (tên hoặc mô tả).
     * Đã fix: trước đây không sử dụng tham số :key trong query.
     */
    @Query("""
                SELECT new com.dev.dungcony.modules.products.dtos.res.ProductAddRes(
                    p.id,
                    p.name,
                    p.price,
                    p.rated,
                    p.img,
                    p.category.id
                )
                FROM Product p
                WHERE p.status = :status
                    AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :key, '%'))
                         OR LOWER(p.description) LIKE LOWER(CONCAT('%', :key, '%')))
            """)
    Page<ProductAddRes> getAllByKeyword(
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
                img.image_url AS image,
                p.category_id AS categoryId
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

    boolean existsByCategoryId(Integer id);

    /**
     * Kiểm tra tất cả productIds có tồn tại hay không.
     * Dùng khi validate danh sách productIds trước khi tạo promotion.
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.id IN :ids AND p.status = :status")
    long countByIdInAndStatus(
            @Param("ids") List<Integer> ids,
            @Param("status") ProductStatus status
    );
}
