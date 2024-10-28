package com.lms.onlinelms.coursemanagement.mapper;

import com.lms.onlinelms.coursemanagement.dto.CategoryDto;
import com.lms.onlinelms.coursemanagement.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    Category toCategoryDto(Category savedCategory);
}
