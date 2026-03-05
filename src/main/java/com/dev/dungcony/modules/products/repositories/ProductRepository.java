package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Load product với category và provider trong 1 query (tránh N+1).
     */
    @Query("""
                SELECT p FROM Product p
                LEFT JOIN FETCH p.category
                LEFT JOIN FETCH p.provider
                WHERE p.id = :id
            """)
    Optional<Product> findByIdWithCategoryAndProvider(@Param("id") Integer id);

    @Query("""
                SELECT new com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes(
                    p.code,
                    p.name,
                    p.price,
                    p.rated,
                    p.img,
                    p.category.code
                )
                FROM Product p
                WHERE p.status = :status
            """)
    Page<ProductSumaryRes> findProductList(
            @Param("status") ProductStatus status,
            Pageable pageable);

    /**
     * Tìm sản phẩm theo keyword (tên hoặc mô tả).
     * Đã fix: trước đây không sử dụng tham số :key trong query.
     */
    @Query("""
                SELECT new com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes(
                    p.code,
                    p.name,
                    p.price,
                    p.rated,
                    p.img,
                    p.category.code
                )
                FROM Product p
                WHERE p.status = :status
                    AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :key, '%'))
                         OR LOWER(p.description) LIKE LOWER(CONCAT('%', :key, '%')))
            """)
    Page<ProductSumaryRes> getAllByKeyword(
            @Param("status") ProductStatus status,
            @Param("key") String key,
            Pageable pageable);

    @Query("""
                SELECT new com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes(
                    p.code,
                    p.name,
                    p.price,
                    p.rated,
                    p.img,
                    p.category.code
                )
                FROM Product p
                JOIN p.category c
                JOIN Category root ON root.id = :categoryId
                WHERE p.status = com.dev.dungcony.modules.products.enums.ProductStatus.ACTIVE
                  AND c.path LIKE CONCAT(root.path, '%')
            """)
    Page<ProductSumaryRes> findAllByCategoryTree(
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

    boolean existsByCategoryId(Integer id);

    /**
     * Kiểm tra tất cả productIds có tồn tại hay không.
     * Dùng khi validate danh sách productIds trước khi tạo promotion.
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.id IN :ids AND p.status = :status")
    long countByIdInAndStatus(
            @Param("ids") List<Integer> ids,
            @Param("status") ProductStatus status);

    Optional<Product> findByCode(String productCode);

    /**
     * Load product với category và provider theo mã sản phẩm (tránh N+1).
     */
    @Query("""
                SELECT p FROM Product p
                LEFT JOIN FETCH p.category
                LEFT JOIN FETCH p.provider
                WHERE p.code = :code
            """)
    Optional<Product> findByCodeWithCategoryAndProvider(@Param("code") String code);
}
