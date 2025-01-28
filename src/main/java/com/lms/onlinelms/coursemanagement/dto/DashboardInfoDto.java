package com.lms.onlinelms.coursemanagement.dto;

import lombok.Data;

@Data
public class DashboardInfoDto {

    private Long coursesCount;
    private int LastWeekCoursesCount;
    private int reviewCoursesCount;
    private int publishCoursesCount;
    private int draftCoursesCount;
    private int archivedCoursesCount;
    private int deletedCoursesCount;
}
