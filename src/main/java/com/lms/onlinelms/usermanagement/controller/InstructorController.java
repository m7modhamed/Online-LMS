package com.lms.onlinelms.usermanagement.controller;

import com.lms.onlinelms.usermanagement.dto.InstructorDto;
import com.lms.onlinelms.usermanagement.dto.InstructorUpdateDto;
import com.lms.onlinelms.usermanagement.mapper.IInstructorMapper;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.service.interfaces.IInstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class InstructorController {
    private final IInstructorService instructorService;
    private final IInstructorMapper instructorMapper;

    @GetMapping("/instructors/{instructorId}")
    public ResponseEntity<InstructorDto> getInstructor(@PathVariable Long instructorId) {
        Instructor instructor = instructorService.getInstructorInfoById(instructorId);

        InstructorDto instructorDto = instructorMapper.toInstructorDto(instructor);

        return ResponseEntity.ok(instructorDto);
    }

    @PutMapping("/instructors/{instructorId}/update")
    public ResponseEntity<InstructorUpdateDto> updateInstructor(@RequestPart("instructorInfo") @Valid InstructorUpdateDto instructorUpdateDto,
                                                   @RequestPart("image") MultipartFile profileImage,
                                                   @PathVariable Long instructorId) {

        Instructor instructor = instructorService.updateInstructor(instructorUpdateDto ,profileImage, instructorId);

        return ResponseEntity.ok(instructorMapper.toInstructorUpdateDto(instructor));
    }

}
