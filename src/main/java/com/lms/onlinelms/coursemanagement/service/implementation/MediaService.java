package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.coursemanagement.model.*;
import com.lms.onlinelms.coursemanagement.repository.FileResourceRepository;
import com.lms.onlinelms.coursemanagement.repository.LessonRepository;
import com.lms.onlinelms.coursemanagement.repository.VideoRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
import com.lms.onlinelms.coursemanagement.service.interfaces.IFileResourceService;
import com.lms.onlinelms.coursemanagement.service.interfaces.ILessonService;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
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

    private final ILessonService lessonService;
    private final VideoRepository videoRepository;
    private final ICourseService courseService;
    private static final String UPLOAD_DIR = "D:/Projects/Online-LMS-Client-nextjs/public";
    private final FileResourceRepository fileResourceRepository;
    private final LessonRepository lessonRepository;
    private final IFileResourceService fileResourceService;

    @Override
    public Content uploadLessonVideo(Long lessonId , MultipartFile file) {
        Content content = null;

        if (file.getContentType() != null && !file.getContentType().startsWith("video"))
        {
            throw new AppException("the file type must be video", HttpStatus.BAD_REQUEST);
        }

        // Find the lesson
        Lesson lesson = lessonService.findLessonById(lessonId);

        //get course
        Course course = lesson.getSection().getCourse();


        //check for the use own this course or not
        courseService.checkInstructorIsOwner(course);

        if(lesson.getVideo() != null){
            throw new AppException("the lesson already has a video ,you have to delete the old video", HttpStatus.BAD_REQUEST);
        }

        String fileUrl = saveFile(file , "/"+course.getId());


        Video video = new Video();
        String[] urlParts = fileUrl.split("\\\\");
        video.setUrl("\\"+ course.getId() + "\\" + urlParts[urlParts.length-1]);
        double videoDuration = extractVideoDuration(fileUrl);
        video.setDuration(videoDuration);

        lesson.setVideo(video);
        content = videoRepository.save(video);

        //update course duration
        course.setDuration(course.getDuration() + videoDuration);
        courseService.save(course);

        return content;
    }

    @Override
    public List<Content> uploadLessonFiles(Long lessonId, List<MultipartFile> files) {
        List<Content> content = new ArrayList<>();

        // Find the lesson
        Lesson lesson = lessonService.findLessonById(lessonId);

        //get course
        Course course = lesson.getSection().getCourse();

        //check for the use own this course or not
        courseService.checkInstructorIsOwner(course);



        for(MultipartFile file : files){
            String fileType = file.getContentType();
            String fileUrl = saveFile(file  , "/"+course.getId());

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


    /*private boolean deleteAllFiles(Long courseId) {
        File directory = new File(UPLOAD_DIR + "/" + courseId);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && !isVideoFile(file)) {
                        file.delete(); // Delete the file
                    }
                }
            }
        }

        return directory.delete(); // Delete the empty directory itself
    }

    private boolean isVideoFile(File file) {

        String[] videoExtensions = {".mp4", ".avi", ".mkv", ".mov", ".flv", ".wmv", ".webm", ".mpg", ".mpeg"};

        String fileName = file.getName().toLowerCase();
        for (String extension : videoExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }*/

    @Override
    public boolean deleteLessonVideo(Long lessonId) {
        Lesson lesson = lessonService.findLessonById(lessonId);
        Course course = lesson.getSection().getCourse();
        Video video = lesson.getVideo();
        boolean isDeleted = deleteVideoFile(video);
        if(isDeleted){
        videoRepository.delete(video);
        lesson.setVideo(null);
        lessonRepository.save(lesson);

        //update course duration
        course.setDuration(course.getDuration() - video.getDuration());
        courseService.save(course);
        }
        return isDeleted;
    }

    @Override
    public Boolean deleteFile(Long lessonId, Long fileId) {
        // Fetch the lesson by ID
        Lesson lesson = lessonService.findLessonById(lessonId);
        Course course = lesson.getSection().getCourse();
        courseService.checkInstructorIsOwner(lesson.getSection().getCourse());

        FileResource file = fileResourceService.getFileById(fileId);

        // Check and remove the file from the file resource list
        boolean fileRemoved = lesson.getFileResource().removeIf(f -> f.getId().equals(file.getId()));

        if (fileRemoved) {
            // Optionally delete the file from the storage (e.g., file system or cloud storage)
            File fileToDelete = new File(UPLOAD_DIR + "/" + course.getId() + "/" + file.getName());
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                fileToDelete.delete();

                // Save the updated lesson object (if changes need to be persisted)
                fileResourceService.deleteFile(file);
                return true;
            }

        }
            return false; // Return false if the file was not found in the resources

    }


    private boolean deleteVideoFile(Video video) {
        if (video == null || video.getUrl() == null || video.getUrl().isEmpty()) {
            throw new AppException("Video file URL is invalid or not provided.", HttpStatus.BAD_REQUEST);
        }

        // Create a File object for the video file
        File videoFile = new File(UPLOAD_DIR + "/"+video.getUrl());

        try {
            if (videoFile.exists()) {
                // Attempt to delete the file
                return videoFile.delete();
            }

        } catch (SecurityException e) {
            throw new AppException("Permission denied to delete the file: " + video.getUrl(), HttpStatus.FORBIDDEN);
        }
        return false;
    }



    public String saveFile(MultipartFile file ,String folderPath) {
        // Create the upload directory if it doesn't exist
       // File uploadDir = new File(UPLOAD_DIR +"/"+course.getId());
        File uploadDir = new File(UPLOAD_DIR + folderPath);

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
