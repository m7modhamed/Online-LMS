package com.lms.onlinelms.coursemanagement.mapper;

import com.lms.onlinelms.coursemanagement.dto.*;
import com.lms.onlinelms.coursemanagement.model.Course;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    Course toCourse(CourseRequestDto courseRequestDto);

    CourseResponseDto toCourseResponseDto(Course course);

    CourseInfoDto toCourseInfoDto(Course courses);
    List<CourseInfoDto> toCourseInfoDto(List<Course> courses);

    @AfterMapping
    default void setEnrolledStudentsNumber(@MappingTarget CourseInfoDto courseInfoDto , Course course) {
        if (course.getEnrolledStudents() != null) {
            courseInfoDto.setEnrolledStudentsNumber(course.getEnrolledStudents().size());
        } else {
            courseInfoDto.setEnrolledStudentsNumber(0);
        }
    }

    @AfterMapping
    default void setEnrolledStudentsNumber(@MappingTarget AdminCourseInfoDto adminCourseInfoDto , Course course) {
        if (course.getEnrolledStudents() != null) {
            adminCourseInfoDto.setEnrolledStudentsNumber(course.getEnrolledStudents().size());
        } else {
            adminCourseInfoDto.setEnrolledStudentsNumber(0);
        }
    }


    List<CourseResponseDto> toCourseResponseDto(List<Course> course);



    List<AdminCourseInfoDto> toAdminCourseInfoDto(List<Course> courses);
    AdminCourseInfoDto toAdminCourseInfoDto(Course courses);

    List<DashboardInfoDto> toDashboardInfoDto(List<Course> courses);
}
