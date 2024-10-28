package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.Category;

public interface ICategoryService {
    Category findById(Long id);

    Category save(Category category);
}
