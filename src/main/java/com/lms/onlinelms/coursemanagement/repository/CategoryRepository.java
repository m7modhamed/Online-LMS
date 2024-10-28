package com.lms.onlinelms.coursemanagement.repository;

import com.lms.onlinelms.coursemanagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
