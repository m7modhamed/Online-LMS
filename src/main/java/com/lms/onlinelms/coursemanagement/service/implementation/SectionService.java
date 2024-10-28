package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.coursemanagement.model.Section;
import com.lms.onlinelms.coursemanagement.repository.SectionRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ISectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SectionService implements ISectionService {

    private final SectionRepository sectionRepository;
    private final ICourseService courseService;

    @Override
    public Section appendSection(Long courseId, Section section) {
        Course course = courseService.findCourseById(courseId);

        courseService.checkInstructorIsOwner(course);

        section.setCourse(course);

        return sectionRepository.save(section);
    }

    @Override
    public Section findSectionById(int sectionId) {
        return sectionRepository.findById(sectionId).orElseThrow(
                ()-> new AppException("section not found", HttpStatus.NOT_FOUND)
        );
    }

}
