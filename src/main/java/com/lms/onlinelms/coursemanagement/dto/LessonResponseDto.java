package com.lms.onlinelms.coursemanagement.dto;
import lombok.Data;
import java.util.List;

@Data
public class LessonResponseDto {

    private Long id;

    private String title;

    private short position;

    private List<ContentResourceDto> contentResource;

    private VideoContentDto videoContent;
}


