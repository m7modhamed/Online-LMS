package com.lms.onlinelms.coursemanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class SectionResponseDto {

    private Long id;

    private String title;

    private String description;

    private short position;

    private List<LessonResponseDto> lessons;
}
