package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.Lesson;

public interface ILessonService {

    Lesson findLessonById(Long id);


    Lesson appendLesson(int sectionId, Lesson lesson);

    void saveLesson(Lesson lesson);
}
