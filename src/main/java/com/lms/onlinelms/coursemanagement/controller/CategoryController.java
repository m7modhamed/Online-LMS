package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.coursemanagement.dto.CategoryDto;
import com.lms.onlinelms.coursemanagement.mapper.ICategoryMapper;
import com.lms.onlinelms.coursemanagement.model.Category;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class CategoryController {

    private final ICategoryService categoryService;
    private final ICategoryMapper ICategoryMapper;

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        Category category= ICategoryMapper.toCategory(categoryDto);

        Category savedCategory = categoryService.save(category);

        return ResponseEntity.ok().body(ICategoryMapper.toCategoryDto(savedCategory));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {

        List<Category> categories = categoryService.getCategories();

        return ResponseEntity.ok().body(ICategoryMapper.categoryListToCategoryDtoList(categories));
    }

}
