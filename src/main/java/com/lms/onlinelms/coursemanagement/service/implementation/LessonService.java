package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.coursemanagement.model.Lesson;
import com.lms.onlinelms.coursemanagement.model.Section;
import com.lms.onlinelms.coursemanagement.repository.LessonRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ILessonService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class LessonService implements ILessonService {

    private final LessonRepository lessonRepository;
    private final ICourseService courseService;
    private final ISectionService sectionService;

    @Override
    public Lesson findLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lesson not found with ID: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Lesson appendLesson(int sectionId, Lesson lesson) {
        Section section = sectionService.findSectionById(sectionId);
        Course course = section.getCourse();

        courseService.checkInstructorIsOwner(course);

        lesson.setSection(section);

        return lessonRepository.save(lesson);
    }
}
