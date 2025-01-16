package com.lms.onlinelms.coursemanagement.service.interfaces;

import com.lms.onlinelms.coursemanagement.model.FileResource;

public interface IFileResourceService {
    FileResource getFileById(Long fileId);

    void deleteFile(FileResource file);
}
