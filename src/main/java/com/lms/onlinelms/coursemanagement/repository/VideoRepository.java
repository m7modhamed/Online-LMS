package com.lms.onlinelms.coursemanagement.repository;

import com.lms.onlinelms.coursemanagement.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
