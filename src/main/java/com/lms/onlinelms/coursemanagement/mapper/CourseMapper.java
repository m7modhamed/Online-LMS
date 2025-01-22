package com.lms.onlinelms.coursemanagement.mapper;


import com.lms.onlinelms.coursemanagement.dto.AdminCourseInfoDto;
import com.lms.onlinelms.coursemanagement.dto.CourseInfoDto;
import com.lms.onlinelms.coursemanagement.dto.CourseRequestDto;
import com.lms.onlinelms.coursemanagement.dto.CourseResponseDto;
import com.lms.onlinelms.coursemanagement.model.Course;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    Course toCourse(CourseRequestDto courseRequestDto);

    CourseResponseDto toCourseResponseDto(Course course);

    List<CourseInfoDto> toCourseInfoDto(List<Course> courses);

    @AfterMapping
    default void setEnrolledStudentsNumber(@MappingTarget CourseInfoDto courseInfoDto, Course course) {
        if (course.getEnrolledStudents() != null) {
            courseInfoDto.setEnrolledStudentsNumber(course.getEnrolledStudents().size());
        } else {
            courseInfoDto.setEnrolledStudentsNumber(0);
        }
    }


    List<CourseResponseDto> toCourseResponseDto(List<Course> course);



    List<AdminCourseInfoDto> toAdminCourseInfoDto(List<Course> courses);
}
