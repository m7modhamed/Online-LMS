package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.coursemanagement.dto.CourseRequestDto;
import com.lms.onlinelms.coursemanagement.dto.CourseSearchCriteria;
import com.lms.onlinelms.coursemanagement.dto.DashboardInfoDto;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.usermanagement.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ICourseService {


    Course createCourse(CourseRequestDto courseRequestDto , MultipartFile image);



    Course publishRequest(Long courseId);


    Course getCourseForStudentById(Long courseId , Long studentId);

    Course publishCourse(Long courseId);


    Course getInstructorCourse(Instructor instructor , long courseId);

    Course findCourseById(long courseId);

    List<Course> getCoursesForReviewing();

    void checkInstructorIsOwner(Course course);

    void archiveCourse(Long courseId) throws AppException;

    void enrollStudentIntoCourse(Long studentId, Long courseId);

    void deleteCourse(Long courseId);

    Course getCourseForAdmin(Long courseId);

    Course save(Course course);

    boolean isStudentEnrolledIntoCourse(Long studentId, Long courseId);

    Page<Course> getEnrolledCoursesForStudent(CourseSearchCriteria searchCriteria,Long studentId, PageRequest pageRequest);

    DashboardInfoDto getAdminDashboardInfo();

    DashboardInfoDto getInstructorDashboardInfo(Long instructorId);

    DashboardInfoDto getStudentDashboardInfo(Long studentId);

    Page<Course> getPublishedCourses(CourseSearchCriteria searchCriteria, PageRequest pageRequest);

    Page<Course> getCoursesForAdmin(CourseSearchCriteria searchCriteria, PageRequest pageRequest);

    Page<Course> getCoursesForInstructor(CourseSearchCriteria searchCriteria,Long instructorId , PageRequest pageRequest);
}
