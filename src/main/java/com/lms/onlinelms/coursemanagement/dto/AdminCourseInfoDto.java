package com.lms.onlinelms.coursemanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.coursemanagement.model.CoverImage;
import com.lms.onlinelms.usermanagement.dto.InstructorDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCourseInfoDto {

    private Long id;

    private String name;

    private String description;

    private String language;

    private InstructorDto instructor;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime lastUpdate;

    private int enrolledStudentsNumber;

    private CoverImage coverImage;

    private double duration;

    private CourseStatus status;

}
