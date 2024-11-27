package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.Category;

import java.util.List;

public interface ICategoryService {
    Category findById(Long id);

    Category save(Category category);

    List<Category> getCategories();
}
