package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.utility.UserUtil;
import com.lms.onlinelms.coursemanagement.dto.CourseInfoDto;
import com.lms.onlinelms.coursemanagement.dto.CourseRequestDto;
import com.lms.onlinelms.coursemanagement.dto.CourseResponseDto;
import com.lms.onlinelms.coursemanagement.mapper.CourseMapper;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.usermanagement.model.Instructor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController {

    private final ICourseService courseService;
    private final CourseMapper courseMapper;


    @PostMapping("/courses")
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody @Valid CourseRequestDto courseRequestDto){
        Course course= courseService.createCourse(courseRequestDto);
        CourseResponseDto courseResponseDto = courseMapper.toCourseResponseDto(course);
        return ResponseEntity.ok().body(courseResponseDto);
    }


    @GetMapping("/courses/{courseId}/publishRequest")
    public ResponseEntity<String> publishCourseRequest(@PathVariable Long courseId){

        courseService.publishRequest(courseId);

        return ResponseEntity.ok("Your course publish request has been successfully submitted. Please allow 2-3 business days for review.");
    }

    @GetMapping("/courses/{courseId}/publish")
    public ResponseEntity<String> publishCourse(@PathVariable Long courseId){

        courseService.publishCourse(courseId);

       return ResponseEntity.ok("the course publish has been successfully.");
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseInfoDto>> getAllCourses(){
        List<Course> courses= courseService.getAllPublishedCourses();

        List<CourseInfoDto> courseInfoDto=courseMapper.toCourseInfoDto(courses);

        return ResponseEntity.ok(courseInfoDto);
    }

    @GetMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<?> getStudentCourseById(@PathVariable Long courseId
            , @PathVariable Long studentId){
        Course course= courseService.getCourseForStudentById(courseId , studentId);

        CourseResponseDto courseResponseDto=courseMapper.toCourseResponseDto(course);

        if(courseResponseDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.ok(courseResponseDto);
    }

    @GetMapping("/instructor/{instructorId}/courses")
    public ResponseEntity<List<CourseInfoDto>> getCoursesForInstructor(@PathVariable long instructorId){
        Instructor instructor =(Instructor) UserUtil.getCurrentUser();
        if(instructorId != instructor.getId()){
            throw new AppException("the instructor id is not correct ,please try again.", HttpStatus.BAD_REQUEST);
        }
        List<Course> courseList= courseService.getCoursesForInstructor(instructor);

        return ResponseEntity.ok(courseMapper.toCourseInfoDto(courseList));
    }

    @GetMapping("/instructor/{instructorId}/courses/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourseForInstructor(
            @PathVariable long instructorId
            , @PathVariable long courseId) {

        Instructor instructor =(Instructor) UserUtil.getCurrentUser();
        if(instructorId != instructor.getId()){
            throw new AppException("the instructor id is not correct ,please try again.", HttpStatus.BAD_REQUEST);
        }
        Course course= courseService.getCourseForInstructor(instructor,courseId);

        CourseResponseDto courseResponseDto =
                courseMapper.toCourseResponseDto(course);

        return ResponseEntity.ok(courseResponseDto);
    }

    @GetMapping("/review/courses")
    public ResponseEntity<List<CourseInfoDto>> getCoursesForReviewing() {

        //Admin admin =(Admin) UserUtil.getCurrentUser();

        List<Course> courses= courseService.getCoursesForReviewing();

        List<CourseInfoDto> courseInfoDto = courseMapper.toCourseInfoDto(courses);

        return ResponseEntity.ok(courseInfoDto);
    }


    @GetMapping("courses/{courseId}/archive")
    public ResponseEntity<String> archiveCourse(@PathVariable Long courseId){

        courseService.archiveCourse(courseId);

        return ResponseEntity.ok("Your course has been archived.");
    }

    @PostMapping("/students/{studentId}/courses/{courseId}/enroll")
    public ResponseEntity<String> enrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {

        courseService.enrollStudentIntoCourse(studentId , courseId);

        return ResponseEntity.ok("Student successfully enrolled in course.");
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId){
        courseService.deleteCourse(courseId);

        return ResponseEntity.ok("Your course has been marked as deleted.");
    }

}
