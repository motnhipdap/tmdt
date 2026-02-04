package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    List<ProductImg> findByProduct(Product product);

    Optional<ProductImg> findProductImgByProduct(Product product);
}
