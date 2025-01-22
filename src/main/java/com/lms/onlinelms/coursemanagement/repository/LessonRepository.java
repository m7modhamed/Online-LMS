package com.lms.onlinelms.coursemanagement.repository;

import com.lms.onlinelms.coursemanagement.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LessonRepository extends JpaRepository<Lesson, Long> {


}
