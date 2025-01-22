package com.lms.onlinelms.coursemanagement.repository;

import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c JOIN c.enrolledStudents s WHERE c.id = :courseId AND s.id = :studentId AND  (c.status = 2 OR c.status = 3)")
    Course findPublishedCourseByIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    List<Course> findAllByStatus(CourseStatus status);

    Course findByStatusAndId(CourseStatus status, Long id);

    List<Course> findAllByInstructorAndStatusNot(Instructor instructor , CourseStatus status);

    Optional<Course> findByInstructorAndId(Instructor instructor, Long id);

    List<Course> findAllByEnrolledStudentsId(Long studentId);
}
