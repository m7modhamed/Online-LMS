package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.coursemanagement.model.Content;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class MediaController {

    private final IMediaService mediaService;

    @PostMapping("/lessons/{lesson_id}/media")
    public ResponseEntity<Content> uploadMedia(
            @PathVariable Long lesson_id,
            @RequestParam("file") MultipartFile file) {

        Content content = mediaService.uploadLessonMedia(lesson_id, file);

        return ResponseEntity.ok(content);
    }
}
