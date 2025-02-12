package com.lms.onlinelms.coursemanagement.repository;

import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.usermanagement.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {

    int countByStatus(CourseStatus status);
    int countByCreatedAtIsAfter(LocalDateTime createdAt);
    int countByCreatedAtIsAfterAndInstructorId(LocalDateTime createdAt, Long instructorId);
    int countByStatusAndInstructorId(CourseStatus status , Long instructorId);

    Long countByInstructorId(Long instructorId);

    List<Course> findAllByStatus(CourseStatus status);

    Course findByStatusAndId(CourseStatus status, Long id);

    Optional<Course> findByInstructorAndId(Instructor instructor, Long id);

    List<Course> findAllByEnrolledStudentsId(Long studentId);

    @Query("SELECT count(c) FROM Course c JOIN c.enrolledStudents s WHERE s.id = :studentId")
    Long countByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT count(c) FROM Course c JOIN c.enrolledStudents s WHERE s.id = :studentId AND c.status = :courseStatus " )
    int countByStatusAndStudentId(@Param("courseStatus") CourseStatus courseStatus,@Param("studentId") Long studentId);

}
