package com.lms.onlinelms.usermanagement.service.implementation;


import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import com.lms.onlinelms.usermanagement.dto.InstructorUpdateDto;
import com.lms.onlinelms.usermanagement.model.Image;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.repository.InstructorRepository;
import com.lms.onlinelms.usermanagement.service.interfaces.IInstructorService;
import com.lms.onlinelms.usermanagement.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorService implements IInstructorService {

    private final InstructorRepository instructorRepository;
    private final IUserService userService;
    private final IMediaService mediaService;

    @Override
    public Instructor updateInstructor(InstructorUpdateDto instructorUpdateDto, MultipartFile profileImage, Long instructorId) {
        Instructor instructor = findInstructorById(instructorId);
        userService.checkIfUserIdCorrect(instructorId);



        instructor.setFirstName(instructorUpdateDto.getFirstName());
        instructor.setLastName(instructorUpdateDto.getLastName());
        instructor.setProfileImage(instructorUpdateDto.getProfileImage());
        instructor.setPhoneNumber(instructorUpdateDto.getPhoneNumber());
        instructor.setSpecialization(instructorUpdateDto.getSpecialization());
        instructor.setAboutMe(instructorUpdateDto.getAboutMe());
        instructor.setGithubUrl(instructorUpdateDto.getGithubUrl());
        instructor.setTwitterUrl(instructorUpdateDto.getTwitterUrl());
        instructor.setFacebookUrl(instructorUpdateDto.getFacebookUrl());
        instructor.setLinkedinUrl(instructorUpdateDto.getLinkedinUrl());

        String imageUrl = mediaService.saveFile(profileImage , "/ProfileImages");
        Image image= new Image();
        image.setName(profileImage.getOriginalFilename());
        image.setType(profileImage.getContentType());
        image.setImageUrl(imageUrl);
        instructor.setProfileImage(image);

        return instructorRepository.save(instructor);
    }


    @Override
    public Instructor getInstructorInfoById(Long instructorId) {

        Instructor instructor = findInstructorById(instructorId);

        userService.checkIfUserIdCorrect(instructorId);

        return instructor;
    }


    private Instructor findInstructorById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found.", HttpStatus.NOT_FOUND));
    }
}
