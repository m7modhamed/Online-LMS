package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.coursemanagement.model.FileResource;
import com.lms.onlinelms.coursemanagement.repository.FileResourceRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.IFileResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class FileResourceService implements IFileResourceService {


    private final FileResourceRepository fileResourceRepository;
    @Override
    public FileResource getFileById(Long fileId) {
        return fileResourceRepository.findById(fileId).orElseThrow(
                ()-> new ResourceNotFoundException("file not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public void deleteFile(FileResource file) {
        fileResourceRepository.delete(file);
    }
}
