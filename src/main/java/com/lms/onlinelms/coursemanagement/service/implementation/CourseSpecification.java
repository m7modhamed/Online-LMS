package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.coursemanagement.model.Category;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.model.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public class CourseSpecification {

    public static Specification<Course> hasName(String keySearch) {
        return (root, query, criteriaBuilder) -> {
            if (keySearch == null || keySearch.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(root.get("name"), "%" + keySearch + "%");
        };
    }
    public static Specification<Course> hasLanguage(String language) {
        return (root, query, criteriaBuilder) -> {
            if (language == null || language.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("language"),  language );
        };
    }

    public static Specification<Course> hasInstructorId(Long instructorId) {
        return (root, query, criteriaBuilder) -> {
            if (instructorId == null || instructorId <= 0) {
                return criteriaBuilder.conjunction();
            }

            Join<Course, Instructor> instructorJoin = root.join("instructor");
            return criteriaBuilder.equal(instructorJoin.get("id"), instructorId);
        };
    }


    public static Specification<Course> hasDescription(String keySearch) {
        return (root, query, criteriaBuilder) -> {
            if (keySearch == null || keySearch.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(root.get("description"), "%" + keySearch + "%");
        };
    }

    public static Specification<Course> hasCategory(List<String> categories) {
        return (root, query, criteriaBuilder) -> {

            if (categories == null || categories.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Course, Category> categoryJoin = root.join("category");

            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(categoryJoin.get("name"));
            for (String category : categories) {
                inClause.value(category.toUpperCase());
            }
            return inClause;
        };
    }



    public static Specification<Course> hasCourseStatus(List<CourseStatus> courseStatus) {
        return (root, query, criteriaBuilder) -> {
            if (courseStatus == null || courseStatus.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            CriteriaBuilder.In<CourseStatus> inClause = criteriaBuilder.in(root.get("status"));
            for (CourseStatus status : courseStatus) {
                inClause.value(status);
            }
            return inClause;
        };
    }

    public static Specification<Course> hasNotCourseStatus(CourseStatus courseStatus) {
        return (root, query, criteriaBuilder) -> {
            if (courseStatus == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.notEqual(root.get("status"),courseStatus);
        };
    }



    public static Specification<Course> hasDurationBetween(double min, double max) {
        return (root, query, criteriaBuilder) -> {

            if (min <= 0 && max <= 0) {
                return criteriaBuilder.conjunction();
            }

            if (min > 0 && max <= 0) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("duration"), min);
            }
            if (min <= 0 && max > 0) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("duration"), max);
            }


            return criteriaBuilder.between(root.get("duration"), min, max);
        };
    }


    public static Specification<Course> hasStudentId(Long studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null || studentId <= 0) {
                return criteriaBuilder.conjunction();
            }

            Join<Course, Student> studentJoin = root.join("enrolledStudents");
            return criteriaBuilder.equal(studentJoin.get("id"), studentId);
        };
    }


}
