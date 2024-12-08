package com.lms.onlinelms.coursemanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime lastUpdate;

}
