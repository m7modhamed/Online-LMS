package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.coursemanagement.dto.CategoryDto;
import com.lms.onlinelms.coursemanagement.mapper.CategoryMapper;
import com.lms.onlinelms.coursemanagement.model.Category;
import com.lms.onlinelms.coursemanagement.service.implementation.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        Category category=categoryMapper.toCategory(categoryDto);

        Category savedCategory = categoryService.save(category);

        return ResponseEntity.ok().body(categoryMapper.toCategoryDto(savedCategory));
    }

}
