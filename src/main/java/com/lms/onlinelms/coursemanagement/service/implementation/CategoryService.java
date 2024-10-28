package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.coursemanagement.model.Category;
import com.lms.onlinelms.coursemanagement.repository.CategoryRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryService implements ICategoryService {

private final CategoryRepository categoryRepository;

@Override
    public Category findById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException("Category not found with ID: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
