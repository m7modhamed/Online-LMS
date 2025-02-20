package com.lms.onlinelms.coursemanagement.controller;


import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.utility.UserUtil;
import com.lms.onlinelms.coursemanagement.dto.LessonRequestDto;
import com.lms.onlinelms.coursemanagement.dto.LessonResponseDto;
import com.lms.onlinelms.coursemanagement.mapper.ILessonMapper;
import com.lms.onlinelms.coursemanagement.model.Lesson;
import com.lms.onlinelms.coursemanagement.service.interfaces.ILessonService;
import com.lms.onlinelms.usermanagement.model.Student;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LessonController {

    private final ILessonService lessonService;
    private final ILessonMapper ILessonMapper;

    @PostMapping("sections/{sectionId}/lessons")
    public ResponseEntity<LessonResponseDto> addLesson(@PathVariable int sectionId
            , @RequestBody @Valid LessonRequestDto lessonRequestDto) {

        Lesson lesson = ILessonMapper.toLesson(lessonRequestDto);

        Lesson appendedLesson = lessonService.appendLesson(sectionId , lesson);

        LessonResponseDto lessonResponseDto = ILessonMapper.toLessonResponseDto(appendedLesson);

        return ResponseEntity.ok(lessonResponseDto);
    }

    //this end point must review because it's use by instructor and in security config for any one
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonResponseDto> getLesson(@PathVariable long lessonId) {

        Lesson lesson = lessonService.findLessonById(lessonId);

        LessonResponseDto lessonResponseDto = ILessonMapper.toLessonResponseDto(lesson);

        return ResponseEntity.ok(lessonResponseDto);
    }

    @GetMapping("/students/{studentId}/lessons/{lessonId}")
    public ResponseEntity<LessonResponseDto> getLessonForStudent(@PathVariable long studentId , @PathVariable long lessonId) {
        Student student =(Student) UserUtil.getCurrentUser();
        if(studentId != student.getId()){
            throw new AppException("the instructor id is not correct ,please try again.", HttpStatus.BAD_REQUEST);
        }
        Lesson lesson = lessonService.getStudentLesson(student,lessonId);

        LessonResponseDto lessonResponseDto = ILessonMapper.toLessonResponseDto(lesson);

        return ResponseEntity.ok(lessonResponseDto);
    }
}
