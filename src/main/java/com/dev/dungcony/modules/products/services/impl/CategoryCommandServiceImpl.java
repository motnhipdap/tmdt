package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.req.CategoryAddReq;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.enums.CategoryStatus;
import com.dev.dungcony.modules.products.exceptions.CategoryCanNotCreateException;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.services.interfaces.CategoryCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void addCategory(CategoryAddReq req) {

        Category parent = null;

        if (req.parentId() != null) {
            parent = categoryRepository.findById(req.parentId())
                    .orElseThrow(CategoryNotFoundException::new);

            if (parent.getStatus() != CategoryStatus.ACTIVE) {
                throw new CategoryCanNotCreateException(
                        "Cannot create sub-category under a hidden category"
                );
            }

            if (productRepository.existsByCategoryId(parent.getId())) {
                throw new CategoryCanNotCreateException(
                        "Cannot create sub-category under a category that already contains products"
                );
            }
        }

        Category category = new Category();
        category.setName(req.name());
        category.setCategoryCode(req.categoryCode());
        category.setDescription(req.description());
        category.setImgUrl(req.img());
        category.setParent(parent);
        category.setIsLeaf(true);

        // LEVEL
        if (parent == null) {
            category.setLevel(0);
        } else {
            category.setLevel(parent.getLevel() + 1);
            parent.setIsLeaf(false);
        }

        // Save lần 1 để lấy ID
        categoryRepository.saveAndFlush(category);

        // Set path rồi dirty checking sẽ update
        if (parent == null) {
            category.setPath("/" + category.getId() + "/");
        } else {
            category.setPath(parent.getPath() + category.getId() + "/");
        }
    }


    @Override
    @Transactional
    public void removeCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (category.getStatus() == CategoryStatus.HIDDEN) {
            return; // already hidden
        }

        category.setStatus(CategoryStatus.HIDDEN);

        // Cascade HIDDEN to all sub-categories using path prefix
        if (category.getPath() != null) {
            categoryRepository.hideAllByPathPrefix(category.getPath());
        }
    }
}
