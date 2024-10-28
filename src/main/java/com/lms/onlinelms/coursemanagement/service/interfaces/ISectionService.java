package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.Section;

public interface ISectionService{


    Section appendSection(Long courseId, Section sections);

    Section findSectionById(int sectionId);
}
