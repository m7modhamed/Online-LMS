package com.lms.onlinelms.coursemanagement.repository;

import com.lms.onlinelms.coursemanagement.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    List<Section> findAllByCourse_Id(Long course_id);
}
