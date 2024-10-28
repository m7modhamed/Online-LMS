package com.lms.onlinelms.coursemanagement.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;
import java.util.List;

@Data
public class CourseRequestDto {


    private String name;

    private String description;

    private String language;

    private List<String> prerequisites;

    private CategoryDto categoryDto;

}
