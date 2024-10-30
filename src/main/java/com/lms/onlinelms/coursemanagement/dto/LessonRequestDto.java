package com.lms.onlinelms.coursemanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LessonRequestDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @Min(1)
    private short position;

}
