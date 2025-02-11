package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.common.utility.UserUtil;
import com.lms.onlinelms.coursemanagement.dto.CourseRequestDto;
import com.lms.onlinelms.coursemanagement.dto.CourseSearchCriteria;
import com.lms.onlinelms.coursemanagement.dto.DashboardInfoDto;
import com.lms.onlinelms.coursemanagement.enums.CourseStatus;
import com.lms.onlinelms.coursemanagement.exception.CourseAccessException;
import com.lms.onlinelms.coursemanagement.exception.IncompleteCourseException;
import com.lms.onlinelms.coursemanagement.exception.UnsuitableCourseStatusException;
import com.lms.onlinelms.coursemanagement.mapper.CourseMapper;
import com.lms.onlinelms.coursemanagement.model.*;
import com.lms.onlinelms.coursemanagement.repository.CourseRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICategoryService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.service.interfaces.IStudentService;
import com.lms.onlinelms.usermanagement.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private final IUserService userService;
    @Autowired
    private IMediaService mediaService;

    @Override
    public Course createCourse(CourseRequestDto courseRequestDto , MultipartFile image) {

        Instructor instructor =(Instructor) UserUtil.getCurrentUser();

        Course course=courseMapper.toCourse(courseRequestDto);

        //add course cover image
        String fileUrl = mediaService.saveFile(image , "/coverImages" );
        CoverImage coverImage = new CoverImage();
        coverImage.setName(image.getName());
        coverImage.setType(image.getContentType());
        coverImage.setImageUrl(fileUrl);

        course.setCoverImage(coverImage);
        course.setStatus(CourseStatus.DRAFT);

        Category category = categoryService.findById(courseRequestDto.getCategory().getId());
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
        // check user id is same with user that logged by token
        userService.checkIfUserIdCorrect(studentId);

        return courseRepository.findByStatusAndId(CourseStatus.PUBLISHED,courseId);
    }

    @Override
    public Course publishCourse(Long courseId) {
        Course course = findCourseById(courseId);
        if(course.getStatus() != CourseStatus.IN_REVIEW) {
            throw new UnsuitableCourseStatusException("The course is not currently in the review stage.", HttpStatus.BAD_REQUEST);
        }
        course.setStatus(CourseStatus.PUBLISHED);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getInstructorCourses(Instructor instructor) {

        return courseRepository.findAllByInstructorAndStatusNot(instructor ,CourseStatus.DELETED);
    }

    @Override
    public Course getInstructorCourse(Instructor instructor, long courseId) {

        Course course =  courseRepository
                .findByInstructorAndId(instructor, courseId).orElseThrow(
                        () -> new ResourceNotFoundException("Course with ID " + courseId + " not found for the given instructor.", HttpStatus.NOT_FOUND)
                );

        if(course.getStatus() == CourseStatus.DELETED) {
            throw new AppException("This course has been removed and is no longer available.", HttpStatus.BAD_REQUEST);
        }
        return course;
    }

    @Override
    public List<Course> getCoursesForReviewing() {

        return courseRepository.findAllByStatus(CourseStatus.IN_REVIEW);
    }


    @Override
    public Course getCourseForAdmin(Long courseId) {
        return findCourseById(courseId);
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCoursesForAdmin() {
        return courseRepository.findAll();
    }

    @Override
    public Course getPublishedCourseById(Long courseId) {
        return courseRepository.findByStatusAndId(CourseStatus.PUBLISHED,courseId);
    }

    @Override
    public boolean isStudentEnrolledIntoCourse(Long studentId, Long courseId) {
        Student student = (Student) UserUtil.getCurrentUser();
        userService.checkIfUserIdCorrect(studentId);
        return student.getCourses().contains(findCourseById(courseId));
    }

    @Override
    public List<Course> getEnrolledCoursesForStudent(Long studentId) {
       return courseRepository.findAllByEnrolledStudentsId(studentId);
    }

    @Override
    public DashboardInfoDto getAdminDashboardInfo() {
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);

        Long CoursesCount=courseRepository.count();
        int LastWeekCoursesCount=courseRepository.countByCreatedAtIsAfter(lastWeek);
        int reviewCoursesCount=courseRepository.countByStatus(CourseStatus.IN_REVIEW);
        int publishCoursesCount=courseRepository.countByStatus(CourseStatus.PUBLISHED);
        int archivedCoursesCount=courseRepository.countByStatus(CourseStatus.ARCHIVED);
        int deletedCoursesCount=courseRepository.countByStatus(CourseStatus.DELETED);
        int draftCoursesCount=courseRepository.countByStatus(CourseStatus.DRAFT);

        return getDashboardInfoDto(CoursesCount, LastWeekCoursesCount, reviewCoursesCount, publishCoursesCount, archivedCoursesCount, deletedCoursesCount, draftCoursesCount);
    }

    @Override
    public DashboardInfoDto getInstructorDashboardInfo(Long instructorId) {
        userService.checkIfUserIdCorrect(instructorId);
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);

        Long CoursesCount=courseRepository.countByInstructorId(instructorId);
        int LastWeekCoursesCount=courseRepository.countByCreatedAtIsAfterAndInstructorId(lastWeek , instructorId);
        int reviewCoursesCount=courseRepository.countByStatusAndInstructorId(CourseStatus.IN_REVIEW , instructorId);
        int publishCoursesCount=courseRepository.countByStatusAndInstructorId(CourseStatus.PUBLISHED, instructorId);
        int archivedCoursesCount=courseRepository.countByStatusAndInstructorId(CourseStatus.ARCHIVED, instructorId);
        int deletedCoursesCount=courseRepository.countByStatusAndInstructorId(CourseStatus.DELETED, instructorId);
        int draftCoursesCount=courseRepository.countByStatusAndInstructorId(CourseStatus.DRAFT, instructorId);

        return getDashboardInfoDto(CoursesCount, LastWeekCoursesCount, reviewCoursesCount, publishCoursesCount, archivedCoursesCount, deletedCoursesCount, draftCoursesCount);
    }

    @Override
    public DashboardInfoDto getStudentDashboardInfo(Long studentId) {
        userService.checkIfUserIdCorrect( studentId);

        Long CoursesCount=courseRepository.countByStudentId(studentId);
        int reviewCoursesCount=courseRepository.countByStatusAndStudentId(CourseStatus.IN_REVIEW , studentId);
        int publishCoursesCount=courseRepository.countByStatusAndStudentId(CourseStatus.PUBLISHED, studentId);
        int archivedCoursesCount=courseRepository.countByStatusAndStudentId(CourseStatus.ARCHIVED, studentId);
        int deletedCoursesCount=courseRepository.countByStatusAndStudentId(CourseStatus.DELETED, studentId);
        int draftCoursesCount=courseRepository.countByStatusAndStudentId(CourseStatus.DRAFT, studentId);

        return getDashboardInfoDto(CoursesCount, 0, reviewCoursesCount, publishCoursesCount, archivedCoursesCount, deletedCoursesCount, draftCoursesCount);
    }

    //pageable
    @Override
    public Page<Course> getPublishedCourses(CourseSearchCriteria criteria, PageRequest pageRequest) {

        Specification<Course> spec= Specification.where(CourseSpecification.hasName(criteria.getSearchKey()))
                .or(CourseSpecification.hasDescription(criteria.getSearchKey()))
                .and(CourseSpecification.hasCourseStatus(CourseStatus.PUBLISHED))
                .and(CourseSpecification.hasCategory(criteria.getCategory()))
                .and(CourseSpecification.hasDurationBetween((criteria.getMinDuration()), criteria.getMaxDuration()));

        return courseRepository.findAll(spec, pageRequest);
    }

    @Override
    public Page<Course> getCoursesForAdmin(CourseSearchCriteria criteria, PageRequest pageRequest) {

        String strCourseStatus = criteria.getStatus();
        CourseStatus courseStatus = !strCourseStatus.isBlank() ? CourseStatus.valueOf(strCourseStatus) : null;
        Specification<Course> spec= Specification.where(CourseSpecification.hasName(criteria.getSearchKey()))
                .or(CourseSpecification.hasDescription(criteria.getSearchKey()))
                .and(CourseSpecification.hasCategory(criteria.getCategory()))
                .and(CourseSpecification.hasCourseStatus(courseStatus))

                .and(CourseSpecification.hasDurationBetween((criteria.getMinDuration()), criteria.getMaxDuration()));

        return courseRepository.findAll(spec, pageRequest);
    }

    @Override
    public Page<Course> getCoursesForInstructor(CourseSearchCriteria criteria, Long instructorId, PageRequest pageRequest) {
        userService.checkIfUserIdCorrect(instructorId);

        Specification<Course> spec= Specification.where(CourseSpecification.hasName(criteria.getSearchKey()))
                .or(CourseSpecification.hasDescription(criteria.getSearchKey()))
                .and(CourseSpecification.hasCategory(criteria.getCategory()))
                .and(CourseSpecification.hasNotCourseStatus(CourseStatus.DELETED))
                .and(CourseSpecification.hasInstructorId(instructorId))
                .and(CourseSpecification.hasDurationBetween((criteria.getMinDuration()), criteria.getMaxDuration()));

        return courseRepository.findAll(spec, pageRequest);    }

    private DashboardInfoDto getDashboardInfoDto(Long coursesCount, int lastWeekCoursesCount, int reviewCoursesCount, int publishCoursesCount, int archivedCoursesCount, int deletedCoursesCount, int draftCoursesCount) {
        DashboardInfoDto dashboardInfoDto=new DashboardInfoDto();
        dashboardInfoDto.setCoursesCount(coursesCount);
        dashboardInfoDto.setPublishCoursesCount(publishCoursesCount);
        dashboardInfoDto.setDeletedCoursesCount(deletedCoursesCount);
        dashboardInfoDto.setReviewCoursesCount(reviewCoursesCount);
        dashboardInfoDto.setArchivedCoursesCount(archivedCoursesCount);
        dashboardInfoDto.setLastWeekCoursesCount(lastWeekCoursesCount);
        dashboardInfoDto.setDraftCoursesCount(draftCoursesCount);

        return dashboardInfoDto;
    }

    private void checkStableCourseForPublishing(Course course) {

        if(course.getStatus() != CourseStatus.DRAFT) {
            throw new UnsuitableCourseStatusException("The course is not currently in the draft stage.", HttpStatus.BAD_REQUEST);
        }

        //replace with IncompleteCourseException
        if(course.getSections().isEmpty()){
            throw new IncompleteCourseException("you have to add at least one section before publish it.",HttpStatus.BAD_REQUEST);
        }

        List<Section> sections = course.getSections();
        sections.forEach((s)->{
            if(s.getLessons().isEmpty()){
                throw new IncompleteCourseException("you have to add at least one lesson in each section before publish it.",HttpStatus.BAD_REQUEST);
            }

            List<Lesson> lessons =s.getLessons();
            lessons.forEach((l)->{
                if(l.getVideo() == null && l.getFileResource().isEmpty()){
                    throw new IncompleteCourseException("you have to add at least one content in each lesson before publish it.",HttpStatus.BAD_REQUEST);
                }
            });

        });

    }

    public Course findCourseById(long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course with ID " + courseId + " not found", HttpStatus.NOT_FOUND));
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
            throw new UnsuitableCourseStatusException("The course is not currently in the publish stage.", HttpStatus.BAD_REQUEST);
        }
        course.setStatus(CourseStatus.ARCHIVED);

        courseRepository.save(course);
    }

    @Override
    public void enrollStudentIntoCourse(Long studentId, Long courseId) {
        Student student = (Student) UserUtil.getCurrentUser();

        // check user id is same with user that logged by token
        userService.checkIfUserIdCorrect(studentId);

        Course course = findCourseById(courseId);

        if(course.getStatus() != CourseStatus.PUBLISHED){
            throw new UnsuitableCourseStatusException("The course is not currently in the publish stage.", HttpStatus.BAD_REQUEST);
        }

        if(student.getCourses().contains(course)){
            throw new AppException("you already enrolled in this course.",HttpStatus.BAD_REQUEST);
        }
        student.getCourses().add(course);
        studentService.saveStudent(student);
    }

    @Override
    public void deleteCourse(Long courseId) {
        Course course = findCourseById(courseId);

        checkInstructorIsOwner(course);

        if(!course.getEnrolledStudents().isEmpty()){
            throw new AppException("Cannot delete the course with ID " + courseId + " because it has enrolled students. You can only archive it.", HttpStatus.BAD_REQUEST);
        }

        if (course.getStatus() == CourseStatus.DELETED) {
            throw new UnsuitableCourseStatusException("Cannot proceed because the course already has been marked as deleted.", HttpStatus.BAD_REQUEST);
        }

        course.setStatus(CourseStatus.DELETED);
        courseRepository.save(course);
    }




}
