package com.lms.onlinelms.coursemanagement.controller;


import com.lms.onlinelms.coursemanagement.dto.LessonRequestDto;
import com.lms.onlinelms.coursemanagement.dto.LessonResponseDto;
import com.lms.onlinelms.coursemanagement.mapper.LessonMapper;
import com.lms.onlinelms.coursemanagement.model.Lesson;
import com.lms.onlinelms.coursemanagement.service.interfaces.ILessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LessonController {

    private final ILessonService lessonService;
    private final LessonMapper lessonMapper;

    @PostMapping("sections/{sectionId}/lessons")
    public ResponseEntity<LessonResponseDto> addLesson(@PathVariable int sectionId
            , @RequestBody @Valid LessonRequestDto lessonRequestDto) {

        Lesson lesson = lessonMapper.toLesson(lessonRequestDto);

        Lesson appendedLesson = lessonService.appendLesson(sectionId , lesson);

        LessonResponseDto lessonResponseDto = lessonMapper.toLessonResponseDto(appendedLesson);

        return ResponseEntity.ok(lessonResponseDto);
    }


    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonResponseDto> getLesson(@PathVariable long lessonId) {

        Lesson lesson = lessonService.findLessonById(lessonId);

        LessonResponseDto lessonResponseDto = lessonMapper.toLessonResponseDto(lesson);

        return ResponseEntity.ok(lessonResponseDto);
    }
}
