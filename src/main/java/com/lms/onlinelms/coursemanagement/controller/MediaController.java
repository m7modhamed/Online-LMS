package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.coursemanagement.model.Content;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MediaController {

    private final IMediaService mediaService;

    @PostMapping("/lessons/{lesson_id}/video")
    public ResponseEntity<Content> uploadVideo(
            @PathVariable Long lesson_id,
            @RequestParam("file") MultipartFile file) {

        Content content = mediaService.uploadLessonVideo(lesson_id, file);

        return ResponseEntity.ok(content);
    }

    @PostMapping("/lessons/{lesson_id}/files")
    public ResponseEntity<List<Content>> uploadFiles(
            @PathVariable Long lesson_id,
            @RequestParam("files") List<MultipartFile> files) {

        List<Content> content = mediaService.uploadLessonFiles(lesson_id, files);

        return ResponseEntity.ok(content);
    }
}
