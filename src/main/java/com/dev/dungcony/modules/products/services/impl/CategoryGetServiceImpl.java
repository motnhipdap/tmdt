package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.res.CategoryRes;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.mappers.CategoryMapper;
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
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryRes> getAllChildren(String code) {
        //
        // Category parent = categoryRepository.findByCode(code)
        // .orElseThrow(() -> new RuntimeException("Category not found with code: " +
        // code));
        //
        // List<Category> categories =
        // categoryRepository.findAllChildrenByPath(parent.getPath());
        List<Category> categories = categoryRepository.findAllChildrenByCode(code);

        return categories.stream().map(categoryMapper::toRes).toList();
    }

    @Override
    public List<CategoryRes> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toRes)
                .toList();
    }

    @Override
    public CategoryRes getByCode(String code) {
        return categoryMapper.toRes(
                categoryRepository.findByCode(code).orElseThrow(CategoryNotFoundException::new));
    }

    @Override
    public CategoryRes getByName(String name) {
        return categoryMapper.toRes(
                categoryRepository.findByName(name).orElseThrow(CategoryNotFoundException::new));
    }
}
