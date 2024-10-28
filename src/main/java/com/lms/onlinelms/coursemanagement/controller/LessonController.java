package com.lms.onlinelms.coursemanagement.controller;


import com.lms.onlinelms.coursemanagement.dto.LessonRequestDto;
import com.lms.onlinelms.coursemanagement.dto.LessonResponseDto;
import com.lms.onlinelms.coursemanagement.mapper.LessonMapper;
import com.lms.onlinelms.coursemanagement.model.Lesson;
import com.lms.onlinelms.coursemanagement.service.interfaces.ILessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LessonController {

    private final ILessonService lessonService;
    private final LessonMapper lessonMapper;

    @PostMapping("sections/{sectionId}/lessons")
    public ResponseEntity<LessonResponseDto> addLesson(@PathVariable int sectionId
            , @RequestBody LessonRequestDto lessonRequestDto) {

        Lesson lesson = lessonMapper.toLesson(lessonRequestDto);

        Lesson appendedLesson = lessonService.appendLesson(sectionId , lesson);

        LessonResponseDto lessonResponseDto = lessonMapper.toLessonResponseDto(appendedLesson);

        return ResponseEntity.ok(lessonResponseDto);
    }
}
