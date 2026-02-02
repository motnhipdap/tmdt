package com.dev.dungcony.modules.products.repositories;

import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    List<ProductImg> findByProduct(Product product);
}
