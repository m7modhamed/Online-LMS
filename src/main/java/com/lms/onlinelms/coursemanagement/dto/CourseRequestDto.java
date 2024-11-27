package com.lms.onlinelms.coursemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class CourseRequestDto {

    @NotBlank
    @Size(min = 5, max = 50)
    private String name;

    @NotBlank
    @Size(min = 60 , max = 600)
    private String description;

    @NotBlank
    @Size(min = 1 , max = 10)
    private String language;

    private List<String> prerequisites;

    private CategoryDto category;

}
