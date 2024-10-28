package com.lms.onlinelms.coursemanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseInfoDto {

    private Long id;

    private String name;

    private String description;

    private String language;

    private LocalDateTime lastUpdate;

}
