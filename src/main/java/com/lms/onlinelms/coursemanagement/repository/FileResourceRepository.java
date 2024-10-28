package com.lms.onlinelms.coursemanagement.repository;

import com.lms.onlinelms.coursemanagement.model.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileResourceRepository extends JpaRepository<FileResource, Long> {
}
