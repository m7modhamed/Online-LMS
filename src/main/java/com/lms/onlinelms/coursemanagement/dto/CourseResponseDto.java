package com.lms.onlinelms.coursemanagement.dto;

import com.lms.onlinelms.usermanagement.dto.InstructorDto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseResponseDto {

    private Long id;

    private String name;

    private String description;

    private String language;

    private List<String> prerequisites;

    private List<SectionResponseDto> sections;

    private CategoryDto category;

    private InstructorDto instructor;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdate;

}
