package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.coursemanagement.dto.CourseRequestDto;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.usermanagement.model.Instructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICourseService {


    Course createCourse(CourseRequestDto courseRequestDto , MultipartFile image);



    Course publishRequest(Long courseId);

    List<Course> getAllPublishedCourses();

    Course getCourseForStudentById(Long courseId , Long studentId);

    Course publishCourse(Long courseId);

    List<Course> getCoursesForInstructor(Instructor instructor);

    Course getCourseForInstructor(Instructor instructor , long courseId);

    Course findCourseById(long courseId);

    List<Course> getCoursesForReviewing();

    void checkInstructorIsOwner(Course course);

    void archiveCourse(Long courseId) throws AppException;

    void enrollStudentIntoCourse(Long studentId, Long courseId);

    void deleteCourse(Long courseId);

    Course getCourseForReviewing(Long courseId);
}
