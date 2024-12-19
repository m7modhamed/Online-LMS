package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.Content;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMediaService {

    Content uploadLessonVideo(Long lessonId, MultipartFile file);

    List<Content> uploadLessonFiles(Long lessonId, List<MultipartFile> files);
}
