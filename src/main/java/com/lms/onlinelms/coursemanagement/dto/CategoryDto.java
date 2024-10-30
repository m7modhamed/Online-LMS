package com.lms.onlinelms.coursemanagement.dto;

import lombok.Data;

@Data
public class CategoryDto {

    private Long id;

   /* @NotBlank
    @Size(min = 1, max = 20)*/
    private String name;

    private String description;
}
