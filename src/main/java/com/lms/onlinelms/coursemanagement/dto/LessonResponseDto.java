package com.lms.onlinelms.coursemanagement.dto;
import lombok.Data;
import java.util.List;

@Data
public class LessonResponseDto {

    private Long id;

    private String title;

    private short position;

    private List<fileResourceDto> fileResource;

    private VideoDto video;
}


