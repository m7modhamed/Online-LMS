package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.coursemanagement.model.*;
import com.lms.onlinelms.coursemanagement.repository.FileResourceRepository;
import com.lms.onlinelms.coursemanagement.repository.VideoRepository;
import com.lms.onlinelms.coursemanagement.service.interfaces.ICourseService;
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

@RequiredArgsConstructor
@Service
@Transactional
public class MediaService implements IMediaService {

    private final ILessonService iLessonService;
    private final VideoRepository videoRepository;
    private final ICourseService courseService;
    private static final String UPLOAD_DIR = "D:/uploaded_files";
    private final FileResourceRepository fileResourceRepository;


    @Override
    public Content uploadLessonMedia(Long lessonId , MultipartFile file) {
        Content content = null;

        // Find the lesson
        Lesson lesson = iLessonService.findLessonById(lessonId);

        //get course
        Course course = lesson.getSection().getCourse();

        //check for the use own this course or not
        courseService.checkInstructorIsOwner(course);


        String fileType = file.getContentType();
        String fileUrl = saveFile(file);

        long oldVideoContentId=0;
        if (fileType != null && fileType.startsWith("video")) {
            Video video = new Video();
            video.setUrl(fileUrl);
            video.setDurationInSecond(extractVideoDuration(fileUrl));

            if(lesson.getVideo() != null){
                File existedVideo=new File(lesson.getVideo().getUrl());
                boolean isDeleted = existedVideo.delete();
                if(!isDeleted){
                   throw new AppException("can not upload the video", HttpStatus.BAD_REQUEST);
                }
                oldVideoContentId = lesson.getVideo().getId();

            }
            lesson.setVideo(video);
            content = videoRepository.save(video);
            videoRepository.deleteById(oldVideoContentId);
        } else if(fileType != null && !fileType.startsWith("video")){
            FileResource fileResource = new FileResource();
            fileResource.setUrl(fileUrl);
            fileResource.setFileType(fileType);
            fileResource.setLesson(lesson);
            content = fileResourceRepository.save(fileResource);
        }

        //Lesson SavedLesson = lessonRepository.save(lesson);

        return content;
    }

    private String saveFile(MultipartFile file) {
        // Create the upload directory if it doesn't exist
        File uploadDir = new File(UPLOAD_DIR);
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
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }


    private double extractVideoDuration(String filePath) {
        try {
            // Initialize FFmpeg
            avutil.av_log_set_level(avutil.AV_LOG_QUIET);
            AVFormatContext formatContext = avformat.avformat_alloc_context();
            if (avformat.avformat_open_input(formatContext, filePath, null, null) != 0) {
                throw new RuntimeException("Could not open video file");
            }
            if (avformat.avformat_find_stream_info(formatContext, (AVDictionary) null) < 0) {
                throw new RuntimeException("Could not retrieve video stream info");
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
