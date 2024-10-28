package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.Content;
import org.springframework.web.multipart.MultipartFile;

public interface IMediaService {
    Content uploadLessonMedia(Long lessonId , MultipartFile file);
}
