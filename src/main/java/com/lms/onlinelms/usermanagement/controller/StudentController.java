package com.lms.onlinelms.usermanagement.controller;


import com.lms.onlinelms.usermanagement.dto.StudentUpdateDto;
import com.lms.onlinelms.usermanagement.mapper.IStudentMapper;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.service.interfaces.IStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class StudentController {


    private final IStudentService studentService;
    private final IStudentMapper studentMapper;

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentUpdateDto> getStudent(@PathVariable Long studentId) {
        Student student = studentService.getStudentInfoById(studentId);

        StudentUpdateDto studentDto = studentMapper.toStudentDto(student);

        return ResponseEntity.ok(studentDto);
    }



    @PutMapping("/students/{studentId}/update")
    public ResponseEntity<StudentUpdateDto> updateStudent(@RequestPart("studentInfo") @Valid StudentUpdateDto studentUpdateDto,
                                                @RequestPart("image") MultipartFile profileImage,
                                                @PathVariable Long studentId) {

        Student student = studentService.updateStudent(studentUpdateDto,profileImage, studentId);

        return ResponseEntity.ok(studentMapper.toStudentDto(student));
    }
}
