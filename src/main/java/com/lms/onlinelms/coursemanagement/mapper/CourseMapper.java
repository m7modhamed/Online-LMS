package com.lms.onlinelms.coursemanagement.mapper;


import com.lms.onlinelms.coursemanagement.dto.AdminCourseInfoDto;
import com.lms.onlinelms.coursemanagement.dto.CourseInfoDto;
import com.lms.onlinelms.coursemanagement.dto.CourseRequestDto;
import com.lms.onlinelms.coursemanagement.dto.CourseResponseDto;
import com.lms.onlinelms.coursemanagement.model.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    Course toCourse(CourseRequestDto courseRequestDto);

    CourseResponseDto toCourseResponseDto(Course course);


    List<CourseInfoDto> toCourseInfoDto(List<Course> courses);

    List<CourseResponseDto> toCourseResponseDto(List<Course> course);

    List<AdminCourseInfoDto> toAdminCourseInfoDto(List<Course> courses);
}
