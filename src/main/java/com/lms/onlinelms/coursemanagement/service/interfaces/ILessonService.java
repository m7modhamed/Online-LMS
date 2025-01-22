package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.Lesson;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.model.User;

public interface ILessonService {

    Lesson findLessonById(Long id);


    Lesson appendLesson(int sectionId, Lesson lesson);

    void saveLesson(Lesson lesson);

    Lesson getStudentLesson(Student student, long lessonId);
}
