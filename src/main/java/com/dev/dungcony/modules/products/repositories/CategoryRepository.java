package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByParent_Id(Integer parentId);

    long countByIdIn(List<Integer> ids);

    Optional<Category> findByCode(String categoryCode);

    Optional<Category> findByName(String name);

    List<Category> findByParent_Id(Integer parentId);

    @Query("""
                SELECT c
                FROM Category c
                WHERE c.path LIKE CONCAT(:path, '/%')
            """)
    List<Category> findAllChildrenByPath(String path);

    @Query(value = """
                SELECT c.*
                FROM category c
                WHERE c.path LIKE CONCAT(
                    (SELECT p.path FROM category p WHERE p.code = :code),
                    '/%'
                )
            """, nativeQuery = true)
    List<Category> findAllChildrenByCode(String code);

    @Query("""
                SELECT c
                FROM Category c
                WHERE c.path LIKE CONCAT(:pathPrefix, '%')
            """)
    List<Category> findSubTree(String pathPrefix);

    @Modifying
    @Query("""
                UPDATE Category c
                SET c.status = 'HIDDEN'
                WHERE c.path LIKE CONCAT(:pathPrefix, '/%')
                AND c.status = 'ACTIVE'
            """)
    void hideAllByPathPrefix(@Param("pathPrefix") String pathPrefix);
}