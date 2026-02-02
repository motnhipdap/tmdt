package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.req.CategoryAddReq;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.exceptions.CategoryCanNotCreateException;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepsitory;
import com.dev.dungcony.modules.products.services.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepsitory productRepsitory;

    @Override
    public void addCategory(CategoryAddReq req) {

        Category parent = categoryRepository.findById(req.parent_id())
                .orElseThrow(CategoryNotFoundException::new);

        if (productRepsitory.existsById(parent.getId()))
            throw new CategoryCanNotCreateException("Cannot create sub-category under a category that already contains products");


        Category category = new Category();
        category.setName(req.name());
        category.setDesc(req.description());
        category.setImgUrl(req.img());
        category.setParent(parent);

        categoryRepository.save(category);
    }

    @Override
    public void removeCategory(Integer id) {
        categoryRepository.deleteById(id);
    }


}
