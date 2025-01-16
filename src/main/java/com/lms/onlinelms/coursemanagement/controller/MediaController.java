package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.coursemanagement.model.Content;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/lessons/{lesson_id}/video")
    public ResponseEntity<String> deleteVideo(
            @PathVariable Long lesson_id) {

        boolean isDeleted = mediaService.deleteLessonVideo(lesson_id);

        return isDeleted ?
                ResponseEntity.ok("video deleted successfully") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST ).body("video deleted fail");
    }

    @PostMapping("/lessons/{lesson_id}/files")
    public ResponseEntity<List<Content>> uploadFiles(
            @PathVariable Long lesson_id,
            @RequestParam("files") List<MultipartFile> files) {

        List<Content> content = mediaService.uploadLessonFiles(lesson_id, files);

        return ResponseEntity.ok(content);
    }


    @DeleteMapping("/lessons/{lesson_id}/files/{file_id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long lesson_id
            ,@PathVariable Long file_id ){

        Boolean isDeleted = mediaService.deleteFile(lesson_id , file_id);

        return isDeleted ? ResponseEntity.ok("file deleted successfully") : ResponseEntity.status(HttpStatus.BAD_REQUEST ).body("file deleted fail");
    }
}
