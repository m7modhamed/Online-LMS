package com.lms.onlinelms.coursemanagement.mapper;

import com.lms.onlinelms.coursemanagement.dto.CategoryDto;
import com.lms.onlinelms.coursemanagement.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

    List<CategoryDto> categoryListToCategoryDtoList(List<Category> categoryList);
    Category toCategory(CategoryDto categoryDto);

    Category toCategoryDto(Category savedCategory);
}
