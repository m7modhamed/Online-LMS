package com.lms.onlinelms.coursemanagement.dto;

import lombok.Data;

@Data
public class VideoContentDto {

    private Long id;

    private String videoUrl;

    private double durationInSecond;
}
