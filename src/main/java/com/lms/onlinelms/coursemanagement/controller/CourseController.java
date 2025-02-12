package com.lms.onlinelms.coursemanagement.controller;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.utility.UserUtil;
import com.lms.onlinelms.coursemanagement.dto.*;
import com.lms.onlinelms.coursemanagement.mapper.CourseMapper;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.usermanagement.model.Instructor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController {

    private final ICourseService courseService;
    private final CourseMapper courseMapper;

    @PostMapping(value = "/courses" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CourseResponseDto> createCourse(
            @RequestPart("course") @Valid CourseRequestDto courseRequestDto,
            @RequestPart("image") MultipartFile coverImage) {

        Course course = courseService.createCourse(courseRequestDto, coverImage);
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

    @PostMapping("/courses/all")
    public ResponseEntity<Page<CourseInfoDto>> getPublishedCourses(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String[] sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @RequestBody CourseSearchCriteria searchCriteria
    ) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        if (sortBy == null || sortBy.length == 0) {
            sortBy = new String[] { "createdAt" };
        }
        Sort sort = Sort.by(direction, sortBy);

        PageRequest pageRequest = PageRequest.of(offset, pageSize, sort);

        Page<Course> coursePage = courseService.getPublishedCourses(searchCriteria, pageRequest);

        Page<CourseInfoDto> responseAuctionPage = coursePage.map(courseMapper::toCourseInfoDto);

        return ResponseEntity.ok(responseAuctionPage);
    }

    // api for admin return page of course
    @PostMapping("/admin/courses")
    public ResponseEntity<Page<AdminCourseInfoDto>> getCoursesForAdmin(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String[] sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @RequestBody CourseSearchCriteria searchCriteria
    ) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        if (sortBy == null || sortBy.length == 0) {
            sortBy = new String[] { "createdAt" };
        }
        Sort sort = Sort.by(direction, sortBy);

        PageRequest pageRequest = PageRequest.of(offset, pageSize, sort);

        Page<Course> coursePage = courseService.getCoursesForAdmin(searchCriteria, pageRequest);

        Page<AdminCourseInfoDto> adminCourseInfoDtos = coursePage.map(courseMapper::toAdminCourseInfoDto);

        return ResponseEntity.ok(adminCourseInfoDtos);
    }

    @GetMapping("/admin/courses/info")
    public ResponseEntity<DashboardInfoDto> getAdminDashboardInfo(){
        return ResponseEntity.ok(courseService.getAdminDashboardInfo());
    }

    @GetMapping("/instructor/{instructorId}/courses/info")
    public ResponseEntity<DashboardInfoDto> getInstructorDashboardInfo(@PathVariable Long instructorId){

        return ResponseEntity.ok(courseService.getInstructorDashboardInfo(instructorId));
    }

    @GetMapping("/student/{studentId}/courses/info")
    public ResponseEntity<DashboardInfoDto> getStudentDashboardInfo(@PathVariable Long studentId){

        return ResponseEntity.ok(courseService.getStudentDashboardInfo(studentId));
    }


    @PostMapping("/students/{studentId}/courses")
    public ResponseEntity<Page<CourseInfoDto>> getCoursesForStudent(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String[] sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @PathVariable long studentId,
            @RequestBody CourseSearchCriteria searchCriteria
    ) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        if (sortBy == null || sortBy.length == 0) {
            sortBy = new String[] { "createdAt" };
        }
        Sort sort = Sort.by(direction, sortBy);

        PageRequest pageRequest = PageRequest.of(offset, pageSize, sort);

        Page<Course> coursePage = courseService.getEnrolledCoursesForStudent(searchCriteria,studentId, pageRequest);

        Page<CourseInfoDto> studentCourseInfoDtos = coursePage.map(courseMapper::toCourseInfoDto);

        return ResponseEntity.ok(studentCourseInfoDtos);
    }


    @GetMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<?> getCourseForStudentById(@PathVariable Long courseId
            , @PathVariable Long studentId){
        Course course= courseService.getCourseForStudentById(courseId , studentId);

        CourseResponseDto courseResponseDto=courseMapper.toCourseResponseDto(course);

        if(courseResponseDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.ok(courseResponseDto);
    }

    // api for instructor return page of course
    @PostMapping("/instructor/{instructorId}/courses")
    public ResponseEntity<Page<CourseInfoDto>> getCoursesForInstructor(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String[] sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @PathVariable long instructorId,
            @RequestBody CourseSearchCriteria searchCriteria
    ) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        if (sortBy == null || sortBy.length == 0) {
            sortBy = new String[] { "createdAt" };
        }
        Sort sort = Sort.by(direction, sortBy);

        PageRequest pageRequest = PageRequest.of(offset, pageSize, sort);

        Page<Course> coursePage = courseService.getCoursesForInstructor(searchCriteria,instructorId, pageRequest);

        Page<CourseInfoDto> instructorCourseInfoDtos = coursePage.map(courseMapper::toCourseInfoDto);

        return ResponseEntity.ok(instructorCourseInfoDtos);
    }

    @GetMapping("/instructor/{instructorId}/courses/{courseId}")
    public ResponseEntity<CourseResponseDto> getInstructorCourse(
            @PathVariable long instructorId
            , @PathVariable long courseId) {

        Instructor instructor =(Instructor) UserUtil.getCurrentUser();
        if(instructorId != instructor.getId()){
            throw new AppException("the instructor id is not correct ,please try again.", HttpStatus.BAD_REQUEST);
        }

        Course course= courseService.getInstructorCourse(instructor,courseId);

        CourseResponseDto courseResponseDto =
                courseMapper.toCourseResponseDto(course);

        return ResponseEntity.ok(courseResponseDto);
    }

    @GetMapping("/review/courses")
    public ResponseEntity<List<AdminCourseInfoDto>> getCoursesForReviewing() {

        List<Course> courses= courseService.getCoursesForReviewing();

        List<AdminCourseInfoDto> courseInfoDto = courseMapper.toAdminCourseInfoDto(courses);

        return ResponseEntity.ok(courseInfoDto);
    }

    @GetMapping("/review/courses/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourseForReviewing(
            @PathVariable Long courseId) {

        Course course= courseService.getCourseForAdmin(courseId);

        CourseResponseDto courseResponseDto = courseMapper.toCourseResponseDto(course);

        return ResponseEntity.ok(courseResponseDto);
    }

    @GetMapping("/courses/{courseId}/archive")
    public ResponseEntity<String> archiveCourse(@PathVariable Long courseId){

        courseService.archiveCourse(courseId);

        return ResponseEntity.ok("Your course has been archived.");
    }

    @PostMapping("/students/{studentId}/courses/{courseId}/enroll")
    public ResponseEntity<String> enrollCourse(@PathVariable Long studentId, @PathVariable Long courseId) {

        courseService.enrollStudentIntoCourse(studentId , courseId);

        return ResponseEntity.ok("you successfully enrolled in the course.");
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId){
        courseService.deleteCourse(courseId);

        return ResponseEntity.ok("Your course has been marked as deleted.");
    }


    @GetMapping("/students/{studentId}/courses/{courseId}/isEnrolled")
    public ResponseEntity<Boolean> isEnrolled(@PathVariable Long studentId, @PathVariable Long courseId){

        boolean isEnrolled = courseService.isStudentEnrolledIntoCourse(studentId , courseId);

        return ResponseEntity.ok(isEnrolled);
    }
}
