package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.ProductImg;
import com.dev.dungcony.modules.products.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.products.repositories.ProductImgRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductImgServiceImpl implements ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;

    @Override
    public void addNew(int pId, String url) {
        if (!productRepository.existsById(pId))
            throw new ProductNotFoundException();

        Product product = new Product();
        product.setId(pId);

        ProductImg pImg = new ProductImg();
        pImg.setProduct(product);
        pImg.setImageUrl(url);

        productImgRepository.save(pImg);
    }

    @Override
    public void remove(int pImgid) {
        if (productImgRepository.existsById(pImgid)) {
            productImgRepository.deleteById(pImgid);
        } else {
            throw new ProductNotFoundException();
        }
    }
}
