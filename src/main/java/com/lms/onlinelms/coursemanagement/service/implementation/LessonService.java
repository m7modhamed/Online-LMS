package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.coursemanagement.exception.CourseAccessException;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.coursemanagement.model.Lesson;
import com.lms.onlinelms.coursemanagement.model.Section;
import com.lms.onlinelms.coursemanagement.repository.LessonRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ILessonService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ISectionService;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.service.interfaces.IUserService;
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
    private final IUserService userService;
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

    @Override
    public void saveLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    @Override
    public Lesson getStudentLesson(Student student, long lessonId) {
        Lesson lesson= findLessonById(lessonId);

        if(!student.getCourses().contains(lesson.getSection().getCourse())) {
            throw new CourseAccessException("you are not have access to this lesson resource");
        }

        return lesson;
    }
}
