package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.coursemanagement.model.Category;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.usermanagement.model.Instructor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseSpecification {


    public static Specification<Course> hasPublished(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status") , status);
    }


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


    public static Specification<Course> hasCourseStatus(CourseStatus courseStatus) {
        return (root, query, criteriaBuilder) -> {
            if (courseStatus == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("status"),courseStatus);
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



    public static Specification<Course> hasDateRange(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (fromDateTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDateTime));
            }


            if (toDateTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDateTime));
            }


            return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
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




}
