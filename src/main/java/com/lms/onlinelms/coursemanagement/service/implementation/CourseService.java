package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.utility.UserUtil;
import com.lms.onlinelms.coursemanagement.dto.CourseRequestDto;
import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.coursemanagement.exception.CourseAccessException;
import com.lms.onlinelms.coursemanagement.mapper.CourseMapper;
import com.lms.onlinelms.coursemanagement.model.Category;
import com.lms.onlinelms.coursemanagement.model.Course;
import com.lms.onlinelms.coursemanagement.model.Section;
import com.lms.onlinelms.coursemanagement.repository.CourseRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICategoryService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.coursemanagement.service.interfaces.IStudentService;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService implements ICourseService {

    private final CourseMapper courseMapper;
    private final ICategoryService categoryService;
    private final CourseRepository courseRepository;
    private final IStudentService studentService;


    @Override
    public Course createCourse(CourseRequestDto courseRequestDto) {

        Instructor instructor =(Instructor) UserUtil.getCurrentUser();

        Course course=courseMapper.toCourse(courseRequestDto);

        course.setStatus(CourseStatus.DRAFT);

        Category category = categoryService.findById(courseRequestDto.getCategoryDto().getId());
        course.setCategory(category);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }


    @Override
    public Course publishRequest(Long courseId) {
        Course course = findCourseById(courseId);

        //check the user must be the owner of this course
        checkInstructorIsOwner(course);

        checkStableCourseForPublishing(course);

        course.setStatus(CourseStatus.IN_REVIEW);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllPublishedCourses() {
        return courseRepository.findAllByStatus(CourseStatus.PUBLISHED);
    }

    @Override
    public Course getCourseForStudentById(Long courseId , Long studentId) {
        Student student = (Student) UserUtil.getCurrentUser();

        // check user id is same with user that logged by token
        checkIfUserIdCorrect(student , studentId);


        return courseRepository.findPublishedCourseByIdAndStudentId(courseId, studentId);
    }

    @Override
    public Course publishCourse(Long courseId) {
        Course course = findCourseById(courseId);
        if(course.getStatus() != CourseStatus.IN_REVIEW) {
            throw new AppException("The course is not currently in the review stage.", HttpStatus.BAD_REQUEST);
        }
        course.setStatus(CourseStatus.PUBLISHED);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getCoursesForInstructor(Instructor instructor) {

        return courseRepository.findAllByInstructor(instructor);
    }

    @Override
    public Course getCourseForInstructor(Instructor instructor,long courseId) {
        return courseRepository
                .findByInstructorAndId(instructor,courseId).orElseThrow(
                        () -> new AppException("The course not found.", HttpStatus.NOT_FOUND)
                );
    }

    @Override
    public List<Course> getCoursesForReviewing() {
        return courseRepository.findAllByStatus(CourseStatus.IN_REVIEW);
    }

    private void checkStableCourseForPublishing(Course course) {
        if(course.getSections().isEmpty()){
            throw new AppException("you have to add at least one section before publish it.",HttpStatus.BAD_REQUEST);
        }

        for(Section section: course.getSections()){
            if(section.getLessons().isEmpty()){
                throw new AppException("you have to add at least one lesson in each section before publish it.",HttpStatus.BAD_REQUEST);
            }
        }
    }

    public Course findCourseById(long courseId){
        return  courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException("Course not found", HttpStatus.NOT_FOUND));
    }

    public void checkInstructorIsOwner(Course course) {
        //check ,the user must be the owner of this course
        Instructor instructor =(Instructor) UserUtil.getCurrentUser();
        if(course.getInstructor() == null || !Objects.equals(course.getInstructor().getId(), instructor.getId())){
            throw new CourseAccessException("You are not authorized to manage this course.");
        }

    }

    @Override
    public void archiveCourse(Long courseId) throws AppException{
        Course course = findCourseById(courseId);

        checkInstructorIsOwner(course);
        if(course.getStatus() != CourseStatus.PUBLISHED){
            throw new AppException("The course is not currently in the publish stage.", HttpStatus.BAD_REQUEST);
        }
        course.setStatus(CourseStatus.ARCHIVED);

        courseRepository.save(course);
    }

    @Override
    public void enrollStudentIntoCourse(Long studentId, Long courseId) {
        Student student = (Student) UserUtil.getCurrentUser();

        // check user id is same with user that logged by token
        checkIfUserIdCorrect(student , studentId);

        Course course = findCourseById(courseId);

        if(course.getStatus() != CourseStatus.PUBLISHED){
            throw new AppException("The course is not currently in the publish stage.", HttpStatus.BAD_REQUEST);
        }
        student.getCourses().add(course);

        studentService.saveStudent(student);
    }

    @Override
    public void deleteCourse(Long courseId) {
        Course course = findCourseById(courseId);

        checkInstructorIsOwner(course);

        if(course.getStatus() == CourseStatus.PUBLISHED){
            throw new AppException("The course is currently in the published stage. You can only archive it.", HttpStatus.BAD_REQUEST);
        }

        if(course.getStatus() == CourseStatus.DRAFT || course.getStatus() == CourseStatus.IN_REVIEW){
            course.setStatus(CourseStatus.DELETED);
            courseRepository.save(course);
        }else {
            throw new AppException("The course can only be deleted if it is in draft or in-review status.", HttpStatus.BAD_REQUEST);

        }

    }

    // check user id is same with user that logged by token
    private void checkIfUserIdCorrect(User user , Long userId){
        if(!user.getId().equals(userId)){
            throw new AppException("the student id is not correct ,please try again.", HttpStatus.BAD_REQUEST);
        }
    }
}
