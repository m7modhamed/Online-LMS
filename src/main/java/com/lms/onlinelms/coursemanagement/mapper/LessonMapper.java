package com.lms.onlinelms.coursemanagement.mapper;

import com.lms.onlinelms.coursemanagement.dto.LessonRequestDto;
import com.lms.onlinelms.coursemanagement.dto.LessonResponseDto;
import com.lms.onlinelms.coursemanagement.model.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    Lesson toLesson(LessonRequestDto lessonRequestDto);

    LessonResponseDto toLessonResponseDto(Lesson appendedLesson);
}
