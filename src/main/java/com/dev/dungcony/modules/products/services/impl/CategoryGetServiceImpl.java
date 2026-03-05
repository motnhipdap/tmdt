package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.res.CategoryRes;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.services.interfaces.CategoryGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryGetServiceImpl implements CategoryGetService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryRes> getAllChildren(String code) {
//
//        Category parent = categoryRepository.findByCode(code)
//                .orElseThrow(() -> new RuntimeException("Category not found with code: " + code));
//
//        List<Category> categories = categoryRepository.findAllChildrenByPath(parent.getPath());
        List<Category> categories = categoryRepository.findAllChildrenByCode(code);

        return categories.stream().map(this::toRes).toList();
    }

    @Override
    public List<CategoryRes> getAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories
                .stream()
                .map(this::toRes)
                .toList();

    }

    @Override
    public CategoryRes getById(Integer id) {
        Category cate = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return toRes(cate);
    }

    @Override
    public CategoryRes getByCode(String code) {
        Category cate = categoryRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Category not found with code: " + code));
        return toRes(cate);
    }

    @Override
    public CategoryRes getByName(String name) {
        Category cate = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + name));
        return toRes(cate);
    }


    private CategoryRes toRes(Category category) {
        return new CategoryRes(
                category.getName(),
                category.getCode(),
                category.getStatus(),
                category.getDescription(),
                category.getImgUrl()
        );
    }
}
