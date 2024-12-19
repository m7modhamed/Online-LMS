package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.coursemanagement.model.*;
import com.lms.onlinelms.coursemanagement.repository.FileResourceRepository;
import com.lms.onlinelms.coursemanagement.repository.VideoRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ILessonService;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import com.lms.onlinelms.usermanagement.repository.InstructorRepository;
import com.lms.onlinelms.usermanagement.service.implementation.UserService;
import lombok.RequiredArgsConstructor;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MediaService implements IMediaService {

    private final ILessonService iLessonService;
    private final VideoRepository videoRepository;
    private final ICourseService courseService;
    private static final String UPLOAD_DIR = "D:/My Projects/React Projects/OnlineLMS/public";
    private final FileResourceRepository fileResourceRepository;
    private final UserService userService;
    private final InstructorRepository instructorRepository;




    @Override
    public Content uploadLessonVideo(Long lessonId , MultipartFile file) {
        Content content = null;

        if (file.getContentType() != null && !file.getContentType().startsWith("video"))
        {
            throw new AppException("the file type must be video", HttpStatus.BAD_REQUEST);
        }

        // Find the lesson
        Lesson lesson = iLessonService.findLessonById(lessonId);

        //get course
        Course course = lesson.getSection().getCourse();


        //check for the use own this course or not
        courseService.checkInstructorIsOwner(course);

        if(lesson.getVideo() != null){
            throw new AppException("the lesson already has a video ,you have to delete the old video", HttpStatus.BAD_REQUEST);
        }

        String fileUrl = saveFile(file , course);


        Video video = new Video();
        String[] urlParts = fileUrl.split("\\\\");
        video.setUrl("\\"+ course.getId() + "\\" + urlParts[urlParts.length-1]);
        video.setDurationInSecond(extractVideoDuration(fileUrl));


        lesson.setVideo(video);
        content = videoRepository.save(video);


        return content;
    }

    @Override
    public List<Content> uploadLessonFiles(Long lessonId, List<MultipartFile> files) {
        List<Content> content = new ArrayList<>();

        // Find the lesson
        Lesson lesson = iLessonService.findLessonById(lessonId);

        //get course
        Course course = lesson.getSection().getCourse();

        //check for the use own this course or not
        courseService.checkInstructorIsOwner(course);

        for(MultipartFile file : files){
            String fileType = file.getContentType();
            String fileUrl = saveFile(file , course);

            FileResource fileResource = new FileResource();
            fileResource.setName(file.getOriginalFilename());
            fileResource.setUrl(fileUrl.substring(fileUrl.lastIndexOf("public")+6));
            fileResource.setType(fileType);
            fileResource.setLesson(lesson);

            FileResource savedFile = fileResourceRepository.save(fileResource);
            content.add(savedFile);

        }

        return content;
    }


    private String saveFile(MultipartFile file , Course course) {
        // Create the upload directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR +"/"+course.getId());
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // Create directories if needed
        }

        // Define the target file path
        String originalFilename = file.getOriginalFilename();
        File targetFile = new File(uploadDir,originalFilename );

        try {
            // Transfer the file to the target location
            file.transferTo(targetFile);
            return targetFile.getAbsolutePath();  // Return the  file
        } catch (IOException e) {
            throw new AppException("Failed to save file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    private double extractVideoDuration(String filePath) {
        try {
            // Initialize FFmpeg
            avutil.av_log_set_level(avutil.AV_LOG_QUIET);
            AVFormatContext formatContext = avformat.avformat_alloc_context();
            if (avformat.avformat_open_input(formatContext, filePath, null, null) != 0) {
                throw new AppException("Could not open video file", HttpStatus.BAD_REQUEST);
            }
            if (avformat.avformat_find_stream_info(formatContext, (AVDictionary) null) < 0) {
                throw new AppException("Could not retrieve video stream info" , HttpStatus.BAD_REQUEST);
            }


            // Get video duration in microseconds
            long durationInMicroseconds = formatContext.duration();

            // Convert microseconds to seconds
            double durationInSeconds = durationInMicroseconds / 1000000.0;

            // Free the format context resources
            avformat.avformat_close_input(formatContext);

            return durationInSeconds;

        } catch (Exception e) {
            return 0.0;  // Return 0.0 if duration extraction fails
        }
    }


}
